import React from 'react';
import Link from "next/link";
import {Card, Divider, Flex, Space} from 'antd';
import LoginForm from "@/components/loginForm";

const LoginContainer: React.FC = () => {
    return (
        <Card
            title={
                <h4 style={{
                    display: "flex",
                    justifyContent: "center",
                    paddingTop: 5,
                    color: "darkslategray"
                }}>
                    Hesabınıza Giriş Yapın
                </h4>
            }
            style={{width: 500}}
        >
            <LoginForm/>
            <Divider/>
            <Flex flex={"center"} justify={"space-evenly"} style={{fontSize: 16}}>
                <Space>
                    Hesabınız yok mu ?
                </Space>
                <Space>
                    <Link href={"/register"}>
                        Hesap Oluştur
                    </Link>
                </Space>
            </Flex>
        </Card>
    )
};

export default LoginContainer;
