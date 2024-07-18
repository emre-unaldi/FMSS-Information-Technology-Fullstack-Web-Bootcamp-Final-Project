"use client"
import React from "react";
import {Layout} from "antd";

const { Header, Footer, Content} = Layout;

type LoginPageLayoutProps = {
    children: React.ReactNode;
}

const LoginPageLayout: React.FC<LoginPageLayoutProps> = ({children}) => {

    return (
        <Layout style={{minHeight: '100vh'}}>
            <Header style={{
                backgroundColor: '#f0f2f5',
                textAlign: 'center',
                padding: '20px' }}
            />
            <Content
                style={{
                    display: 'flex',
                    justifyContent: 'center',
                    alignItems: 'center',
                    height: '80vh'
                }}
            >
                {children}
            </Content>
            <Footer
                style={{
                    textAlign: 'center',
                    backgroundColor: '#f0f2f5',
                    fontSize: 16
                }}
            >
                ©2024 Created by Emre Unaldi
            </Footer>
        </Layout>
    )
}

export default LoginPageLayout;