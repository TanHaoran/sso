package com.jerry.sso.server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * Created with IntelliJ IDEA.
 * User: Jerry
 * Date: 2018/5/8
 * Time: 20:27
 * Description: 认证服务器配置
 */
@Configuration
@EnableAuthorizationServer
public class SsoAuthorizationConfig extends AuthorizationServerConfigurerAdapter {

    @Bean
    public TokenStore jwtTokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    /**
     * 做一些Token生成中密钥的配置
     *
     * @return
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
        // 设置签名密钥
        accessTokenConverter.setSigningKey("jerry");
        return accessTokenConverter;
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("jerry1")
                .secret("jerrysecret1")
                .authorizedGrantTypes("authorization_code", "refresh_token")
                .scopes("all")

                .and()
                .withClient("jerry2")
                .secret("jerrysecret2")
                .authorizedGrantTypes("authorization_code", "refresh_token")
                .scopes("all");
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(jwtTokenStore()).accessTokenConverter(jwtAccessTokenConverter());
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        // Spring Security的授权表达式
        // 表示当访问tokenKey的时候，需要身份认证
        security.tokenKeyAccess("isAuthenticated()");
    }
}
