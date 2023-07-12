// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

import "@openzeppelin/contracts/access/Ownable.sol";
import "@openzeppelin/contracts/token/ERC721/extensions/ERC721URIStorage.sol";

contract NFTCAM is ERC721URIStorage, Ownable {
    event Mint(uint256);
    event Burn(uint256 tokenId);

    uint256 private _tokenIdCounter;

    constructor(
        string memory _name,
        string memory _symbol
    ) ERC721(_name, _symbol) {
        _tokenIdCounter = 0;
    }

    // Actions ============================================================

    // recipient는 민팅될 지갑 주소, tokenURI는 새로 만들어진 IPFS 주소
    function mintNFT(address recipient, string memory tokenURI) external onlyOwner {

        _tokenIdCounter = _tokenIdCounter + 1;

        _safeMint(recipient, _tokenIdCounter);
        _setTokenURI(_tokenIdCounter, tokenURI);

        emit Mint(_tokenIdCounter);
    }

    function burnNFT(uint256 tokenId) external onlyOwner {
        _burn(tokenId);

        emit Burn(tokenId);
    }
}