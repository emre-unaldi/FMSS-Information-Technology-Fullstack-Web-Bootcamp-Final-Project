"use client"
import React from "react";
import Link from "next/link";
import {useRouter} from "next/navigation";
import Cookies from "universal-cookie";
import {Avatar, Dropdown, Layout, type MenuProps, Space, Tag, Typography} from "antd";
import {LogoutOutlined, UserOutlined} from "@ant-design/icons";
import {logout} from "@/services/auth";
import {FaHome} from "react-icons/fa";

const {Header, Content, Footer} = Layout;

type DashboardLayoutProps = {
    children: React.ReactNode;
}

const DashboardLayout: React.FC<DashboardLayoutProps> = ({children}) => {
    const router = useRouter();
    const cookies = new Cookies();

    const handleLogout = async () => {
        try {
            const accessToken: string = await cookies.get("jwt-access-token");
            const response = await logout(accessToken);

            if (response?.success) {
                cookies.remove("jwt-access-token");
                router.push("/login");
            }
        } catch (error) {
            console.error(error);
        }
    }

    const items: MenuProps['items'] = [
        {
            key: '1',
            label: (
                <Link href={"/dashboard"}>
                    Profile
                </Link>
            ),
            icon: <UserOutlined style={{fontSize: 16}}/>,
            style: {backgroundColor: '#86CF68', color: 'white', fontSize: 16, margin: 2}
        },
        {
            key: '2',
            label: (
                <Space onClick={handleLogout}>
                    Logout
                </Space>
            ),
            icon: <LogoutOutlined style={{fontSize: 16}}/>,
            style: {backgroundColor: '#fa4b64', color: 'white', fontSize: 16, margin: 2}
        }
    ];

    return (
        <Layout style={{minHeight: '100vh', display: 'flex', flexDirection: 'column'}}>
            <Header
                style={{
                    position: 'sticky',
                    top: 0,
                    zIndex: 1,
                    width: '100%',
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'space-between',
                    backgroundColor: "#007bff"
                }}
            >
                <Space
                    style={{
                        display: 'flex',
                        justifyContent: "center",
                        alignItems: 'center',
                        marginTop: 10,
                        color: "white"
                    }}
                >
                    <FaHome fontSize={25}/>
                    <Typography.Title
                        level={2}
                        style={{
                            fontWeight: "initial",
                            color: "white"
                        }}
                    >
                        RealEstate
                    </Typography.Title>
                </Space>
                <Space>
                    <Tag color={"blue"}
                         style={{
                             fontSize: 16,
                             padding: 10
                         }}
                    >
                        Emre Ünaldı
                    </Tag>
                    <Dropdown menu={{items}} trigger={['click']} placement={"bottom"}>
                        <a onClick={(e) => e.preventDefault()}>
                            <Avatar
                                style={{backgroundColor: 'gray', marginBottom: 6}}
                                icon={<UserOutlined/>}
                                size={"large"}
                            />
                        </a>
                    </Dropdown>
                </Space>
            </Header>
            <Content style={{padding: '50px'}}>
                {children}
            </Content>
            <Footer
                style={{
                    textAlign: 'center',
                    backgroundColor: '#007bff',
                    fontSize: 18,
                    fontWeight: "lighter",
                    color: "white"
                }}
            >
                ©{new Date().getFullYear()} Created by Emre Unaldi
            </Footer>
        </Layout>
    )
}

export default DashboardLayout;