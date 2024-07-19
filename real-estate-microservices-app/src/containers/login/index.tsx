import React from 'react';
import Link from "next/link";
import {Button, Card, Divider, Flex, Space} from 'antd';
import LoginForm from "@/components/loginForm";

const LoginContainer: React.FC = () => {
    return (
        <Card
            title={
                <span
                    style={{
                        display: "flex",
                        justifyContent: "center",
                        paddingTop: 5,
                        color: "darkslategray",
                        fontSize: 25,
                    }}
                >
                    Login to Your Account
                </span>
            }
            style={{width: 500}}
        >
            <LoginForm/>
            <Divider/>
            <Flex flex={"center"} justify={"space-evenly"} style={{fontSize: 16}}>
                <Space>
                    Don't have an account?
                </Space>
                <Space>
                    <Button
                        type={"text"}
                        style={{
                            color: "#077bff",
                            fontSize: 16
                        }}
                    >
                        <Link href={"/register"}>
                            Create Account
                        </Link>
                    </Button>
                </Space>
            </Flex>
        </Card>
    )
};

export default LoginContainer;
