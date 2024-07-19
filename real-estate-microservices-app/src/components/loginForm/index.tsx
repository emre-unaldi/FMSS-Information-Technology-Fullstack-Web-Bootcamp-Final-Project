"use client"
import React from "react";
import {Button, Form, Input, message} from "antd";
import {LockOutlined, UserOutlined} from "@ant-design/icons";
import {useRouter} from "next/navigation";
import {IUserLoginData, login} from "@/services/auth";

type LoginFormValues = {
    username: string,
    password: string,
}

const LoginForm: React.FC = () => {
    const [buttonLoading, setButtonLoading] = React.useState(false);
    const [messageApi, contextHolder] = message.useMessage();
    const key = 'login';
    const router = useRouter()

    const onFinish = async (values: LoginFormValues) => {
        setButtonLoading(true)
        messageApi.open({
            key,
            type: 'loading',
            content: 'User logged in...',
        });
        const {username, password} = values;
        const formData: IUserLoginData = {username, password};

        try {
            const response = await login(formData)
            console.log(response)

            if (response?.success) {
                const accessToken = response?.data?.accessToken;
                localStorage.setItem("jwt-access-token", accessToken);

                setButtonLoading(false)
                setTimeout(() => {
                    messageApi.open({
                        key,
                        type: 'success',
                        content: 'User login successful. Redirecting...',
                        duration: 2,
                    });
                }, 1000);
                router.push("/home")
            } else {
                setButtonLoading(false)
                setTimeout(() => {
                    messageApi.open({
                        key,
                        type: 'error',
                        content: 'User login failed. Please try again',
                        duration: 2,
                    });
                }, 1000);
            }
        } catch (error) {
            setButtonLoading(false)
            setTimeout(() => {
                messageApi.open({
                    key,
                    type: 'error',
                    content: 'User login failed. Please try again',
                    duration: 2,
                });
            }, 1000);
            console.error(error)
        }
    };

    const onFinishFailed = (errorInfo: any) => {
        console.log('Failed:', errorInfo);
    };

    return (
        <>
            {contextHolder}
            <Form
                name="login"
                onFinish={onFinish}
                onFinishFailed={onFinishFailed}
                layout="vertical"
                style={{marginTop: 15}}
            >
                <Form.Item
                    name="username"
                    rules={[{required: true, message: 'Username required!'}]}
                    hasFeedback
                >
                    <Input placeholder="Username" prefix={<UserOutlined/>} size={"large"}/>
                </Form.Item>

                <Form.Item
                    name="password"
                    rules={[
                        {required: true, message: 'Password required!'},
                        {min: 8, message: 'Your password must be at least 8 characters!'}
                    ]}
                    hasFeedback
                >
                    <Input.Password placeholder="Password" prefix={<LockOutlined/>} size={"large"}/>
                </Form.Item>

                <Form.Item>
                    <Button
                        type="primary"
                        htmlType="submit"
                        loading={buttonLoading}
                        block
                        size={"large"}
                        style={{
                            fontSize: 20,
                            marginTop: 20
                        }}
                    >
                        Login
                    </Button>
                </Form.Item>
            </Form>
        </>
    )
}

export default LoginForm