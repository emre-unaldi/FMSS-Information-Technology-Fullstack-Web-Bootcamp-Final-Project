"use client"
import React from "react";
import {Layout} from "antd";
import {AntdRegistry} from '@ant-design/nextjs-registry';

const {Header, Footer, Content} = Layout;

type LoginPageLayoutProps = {
    children: React.ReactNode;
}

const LoginPageLayout: React.FC<LoginPageLayoutProps> = ({children}) => {
    return (
        <Layout style={{minHeight: '100vh'}}>
            <Header
                style={{
                    backgroundColor: '#f0f2f5',
                    textAlign: 'center',
                    padding: '20px'
                }}
            />
            <Content
                style={{
                    display: 'flex',
                    justifyContent: 'center',
                    alignItems: 'center',
                    height: '80vh'
                }}
            >
                <AntdRegistry>
                    {children}
                </AntdRegistry>
            </Content>
            <Footer
                style={{
                    textAlign: 'center',
                    backgroundColor: '#f0f2f5',
                    fontSize: 16,
                    fontWeight: "lighter"
                }}
            >
                ©2024 - Designed By Emre Unaldi
            </Footer>
        </Layout>
    )
}

export default LoginPageLayout;