package com.example.nftcam.api.service.WEB3;

import com.example.nftcam.exception.custom.CustomException;
import com.example.nftcam.web3.NFTCAM;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.gas.StaticGasProvider;

import java.math.BigInteger;

@Slf4j
@Service
public class ContractService {

    @Value("${infura.M_API_URL}")
    private String MUMBAI_INFURA_API_URL;

    @Value("${infura.S_API_URL}")
    private String SEPOLIA_INFURA_API_URL;

    @Value("${metamask.PRIVATE_KEY}")
    private String PRIVATE_KEY;

    @Value("${metamask.S_CONTRACT_ADDRESS}")
    private String S_CONTRACT_ADDRESS;

    @Value("${metamask.M_CONTRACT_ADDRESS}")
    private String M_CONTRACT_ADDRESS;

    public Web3j mumbaiWeb3j() {
        return Web3j.build(new HttpService(MUMBAI_INFURA_API_URL));
    }
    public Web3j sepoliaWeb3j() { return Web3j.build(new HttpService(SEPOLIA_INFURA_API_URL)); }

    private Credentials credentials() {
        BigInteger privateKeyInBT = new BigInteger(PRIVATE_KEY, 16);
        return Credentials.create(ECKeyPair.create(privateKeyInBT));
    }

    public NFTCAM multichainNft(int chainId) {
        try {
            if (chainId == 11155111) {
                BigInteger gasPrice = sepoliaWeb3j().ethGasPrice().send().getGasPrice();
                BigInteger gasLimit = BigInteger.valueOf(4000000);
                StaticGasProvider gasProvider = new StaticGasProvider(gasPrice, gasLimit);

                RawTransactionManager rawManager = new RawTransactionManager(
                        sepoliaWeb3j(), credentials(), chainId
                );

                return NFTCAM.load(S_CONTRACT_ADDRESS, sepoliaWeb3j(), rawManager, gasProvider);
            } else {
                BigInteger gasPrice = mumbaiWeb3j().ethGasPrice().send().getGasPrice();
                BigInteger gasLimit = BigInteger.valueOf(4000000);
                StaticGasProvider gasProvider = new StaticGasProvider(gasPrice, gasLimit);

                RawTransactionManager rawManager = new RawTransactionManager(
                        mumbaiWeb3j(), credentials(), chainId
                );

                return NFTCAM.load(M_CONTRACT_ADDRESS, mumbaiWeb3j(), rawManager, gasProvider);
            }
        } catch (Exception e) {
            throw CustomException.builder().httpStatus(HttpStatus.BAD_REQUEST).message("멀티 체인 에러 : " + e.getMessage()).build();
        }
    }
}
