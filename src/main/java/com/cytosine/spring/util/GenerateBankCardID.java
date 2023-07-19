package com.cytosine.spring.util;

import java.util.Random;
import java.util.UUID;

public class GenerateBankCardID {
    //生成虚拟银行卡号
    public static String accountId(){
        Random random = new Random();
        String card = "5462";
        for (int i=0;i<12;i++){
            int n = random.nextInt(10) + 0;
            card += Integer.toString(n);
        }
        return card;
    }

    public static String get16UUID(){
        UUID id=UUID.randomUUID();
        String[] idd=id.toString().split("-");
        return idd[0]+ idd[1];
    }
}
