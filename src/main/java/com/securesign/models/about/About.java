package com.securesign.models.about;

public class About{

    public static String aboutIntro(){
        return
                """
                This project is developed by Md. Nazmul Hossain and Md. Shajalal, students of
                Green University of Bangladesh, pursuing a B.Sc. in Computer Science and Engineering (CSE),
                As part of our Computer and Cyber Security course, we built this JavaFX application to demonstrate 
                document signing, hashing value generate\s
                
                The implemented modules include:
                    - Generate RSA key pair
                    - Generate SHA-256 hash value
                    - Document Signing (Hash+Private Key)
                    - Store into Local Storage
                    - Verifying the Hash Value
                
                Our goal is to provide a clear and interactive understanding of these fundamental data 
                security and integrity mechanisms.
                """;
    }

    public static String aboutNazmul(){

        return
                """
                Md. Nazmul Hossain
                ID: 223002089
                Mobile: 01743-886186
                Email: 223002089@student.green.ac.bd
                GitHub: nazmul-1117
                Mirpur 2, Dhaka 1216, Bangladesh
                """;
    }

    public static String aboutFuad(){
        return
                """
                Md. Shajalal
                ID: 223002088
                Mobile: 01609-873085
                Email: 223002088@student.green.ac.bd
                GitHub: MdShajalalsojib
                Kanchan, Rupganj, Narayanganj, Bangladesh
                """;
    }
}