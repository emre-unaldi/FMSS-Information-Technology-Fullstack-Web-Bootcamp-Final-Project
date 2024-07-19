"use client"
import React from 'react';
import Link from "next/link";
import {Card, Divider, Space, Flex, Button} from 'antd';
import RegisterForm from "@/components/registerForm";

const RegisterContainer: React.FC = () => {
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
                    Create New Account
                </span>
            }
            style={{width: 500}}
        >
            <RegisterForm/>
            <Divider/>
            <Flex flex={"center"} justify={"space-evenly"} style={{fontSize: 16}}>
                <Space>
                    Do you have an account?
                </Space>
                <Space>
                    <Button
                        type={"text"}
                        style={{
                            color: "#077bff",
                            fontSize: 16
                        }}
                    >
                        <Link href={"/login"}>
                            Login
                        </Link>
                    </Button>
                </Space>
            </Flex>
        </Card>
    );
};

export default RegisterContainer;
