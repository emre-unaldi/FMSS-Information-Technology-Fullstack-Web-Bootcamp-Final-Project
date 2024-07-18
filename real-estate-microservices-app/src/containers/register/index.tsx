"use client"
import React from 'react';
import Link from "next/link";
import {Card, Divider, Space, Flex} from 'antd';
import RegisterForm from "@/components/registerForm";

const RegisterContainer: React.FC = () => {
    return (
        <Card
            title={
                <h4 style={{
                    display: "flex",
                    justifyContent: "center",
                    paddingTop: 5,
                    color: "darkslategray"
                }}>
                    Yeni Hesap Oluştur
                </h4>
            }
            style={{ width: 500 }}
        >
            <RegisterForm />
            <Divider />
            <Flex flex={"center"} justify={"space-evenly"} style={{fontSize: 16}}>
                <Space>
                    Hesabınız var mı ?
                </Space>
                <Space>
                    <Link href={"/login"}>
                        Giriş Yap
                    </Link>
                </Space>
            </Flex>
        </Card>
    );
};

export default RegisterContainer;
