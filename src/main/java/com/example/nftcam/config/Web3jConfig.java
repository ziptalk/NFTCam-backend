package com.example.nftcam.config;

import com.example.nftcam.web3.NFTCAM;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.gas.StaticGasProvider;

import java.io.IOException;
import java.math.BigInteger;

@Slf4j
@Configuration
public class Web3jConfig {

    @Value("${infura.API_URL}")
    private String INFURA_API_URL;

    @Value("${metamask.PRIVATE_KEY}")
    private String PRIVATE_KEY;

    @Value("${metamask.CONTRACT_ADDRESS}")
    private String CONTRACT_ADDRESS;

    @Bean
    public Web3j web3j() {
        return Web3j.build(new HttpService(INFURA_API_URL));
    }

    @Bean
    public Credentials credentials() {
        BigInteger privateKeyInBT = new BigInteger(PRIVATE_KEY, 16);
        return Credentials.create(ECKeyPair.create(privateKeyInBT));
    }

    @Bean
    public NFTCAM nft() throws IOException {
        BigInteger gasPrice = web3j().ethGasPrice().send().getGasPrice();
        log.info("gasPrice: {}", gasPrice);
        BigInteger gasLimit = BigInteger.valueOf(4000000);
        StaticGasProvider gasProvider = new StaticGasProvider(gasPrice, gasLimit);

        int chainId = 11155111;

        RawTransactionManager rawManager = new RawTransactionManager(
                web3j(), credentials(), chainId
        );

        return NFTCAM.load(CONTRACT_ADDRESS, web3j(), rawManager, gasProvider);
    }
}
