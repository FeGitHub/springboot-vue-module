package com.company.project.service.impl;

import com.company.project.core.AbstractService;
import com.company.project.master.dao.UserConfigMapper;
import com.company.project.master.model.UserConfig;
import com.company.project.service.UserConfigService;
import com.company.project.utils.ECCUtil;
import com.company.project.utils.UuidUtils;
import com.company.project.vo.userConfig.UserConfigVo;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.security.PrivateKey;
import java.security.PublicKey;


/**
 * Created by CodeGenerator on 2023/11/12.
 */
@Service
@Transactional
public class UserConfigServiceImpl extends AbstractService<UserConfig> implements UserConfigService {
    @Resource
    private UserConfigMapper userConfigMapper;

    private String publicKeyStr = "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEpWUK8fn6TtPb02MiVBWtoEbqfgNitjoaHvnD1fVRQOoJ3ZSfagE9a92D4xRv78/qzAlC8cyjrP4efaKHyXK1Iw==";
    private String privateKeyStr = "MIGHAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBG0wawIBAQQgO8W8taCO4M/EQRrJxopcnMgWQd0Y1KcDmTVvC9hrANuhRANCAASlZQrx+fpO09vTYyJUFa2gRup+A2K2Ohoe+cPV9VFA6gndlJ9qAT1r3YPjFG/vz+rMCULxzKOs/h59oofJcrUj";


    public void addUserConfig(UserConfigVo userConfigVo) throws Exception {
        UserConfig userConfig = new UserConfig();
        userConfig.setId(UuidUtils.getUuid());
        PublicKey publicKey = ECCUtil.getPublicKey(publicKeyStr);
        PrivateKey privateKey = ECCUtil.getPrivateKey(privateKeyStr);
        byte[] userLoginEncrypt = ECCUtil.eccEncrypt(publicKey, userConfigVo.getUserLogin().getBytes());
        byte[] userTokenEncrypt = ECCUtil.eccEncrypt(publicKey, userConfigVo.getUserToken().getBytes());
        userConfig.setUserLogin(Base64.encodeBase64String(userLoginEncrypt));
        userConfig.setUserToken(Base64.encodeBase64String(userTokenEncrypt));
        userConfig.setUserRemark(userConfigVo.getUserRemark());
        //对应解密结果
        System.out.println("userLogin:" + new String(ECCUtil.eccDecrypt(privateKey, Base64.decodeBase64(userConfig.getUserLogin()))));
        System.out.println("userToken:" + new String(ECCUtil.eccDecrypt(privateKey, Base64.decodeBase64(userConfig.getUserToken()))));
        this.save(userConfig);
    }


    public void analysisUserConfig(String id) throws Exception {
        UserConfig userConfig = findById(id);
        PrivateKey privateKey = ECCUtil.getPrivateKey(privateKeyStr);
        System.out.println("userRemark:" + userConfig.getUserRemark());
        System.out.println("userLogin:" + new String(ECCUtil.eccDecrypt(privateKey, Base64.decodeBase64(userConfig.getUserLogin()))));
        System.out.println("userToken:" + new String(ECCUtil.eccDecrypt(privateKey, Base64.decodeBase64(userConfig.getUserToken()))));
    }
}
