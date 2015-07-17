package org.operationspark.hallebotgenerator.models;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HalleBot {
    public final int intelligence;
    public final int vitality;
    public final int strength;
    public final int agility;
    public final String name;

    public HalleBot(String name, int intelligence, int vitality, int strength, int agility) {
        this.intelligence = intelligence;
        this.vitality = vitality;
        this.strength = strength;
        this.agility = agility;
        this.name = name;
    }

    public static HalleBot build(String name, Location location){

        if(location != null){
            String seed = getSeed(location);

            int agility = Integer.parseInt(seed.substring(0, 4), 16) % 3;
            int vitality = Integer.parseInt(seed.substring(5, 8), 16) % 8;

            int intelligence = Integer.parseInt(seed.substring(9, 12), 16) % 6;
            int strength = Integer.parseInt(seed.substring(13, 16), 16) % 8;

            return new HalleBot(name, intelligence, vitality, strength, agility);
        }

        return null;
    }

    private static String getSeed(Location location){
        return md5(Double.toString(location.latitude + location.longitude));
    }

    public static String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
