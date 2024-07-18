"use client"
import React from "react";
import {Button, Form, Input} from "antd";
import {LockOutlined, UserOutlined} from "@ant-design/icons";
import {useRouter} from "next/navigation";

type LoginFormValues = {
    username: string;
    password: string;
}

const LoginForm: React.FC = () => {
    const router = useRouter()

    const onFinish = (values: LoginFormValues) => {
        console.log('Success:', values);
        alert(JSON.stringify(values));
        router.push('/');
    };

    const onFinishFailed = (errorInfo: any) => {
        console.log('Failed:', errorInfo);
    };

    return (
        <Form
            name="login"
            initialValues={{ }}
            onFinish={onFinish}
            onFinishFailed={onFinishFailed}
            layout="vertical"
            style={{ marginTop: 15 }}
        >
            <Form.Item
                name="username"
                rules={[{required: true, message: 'Kullanıcı adı gereklidir!'}]}
                hasFeedback
            >
                <Input placeholder="Kullanıcı Adı" prefix={<UserOutlined/>} size={"large"} />
            </Form.Item>

            <Form.Item
                name="password"
                rules={[
                    { required: true, message: 'Şifre gereklidir!'},
                    { min: 8, message: 'Şifreniz en az 8 karakter olmalıdır.'}
                ]}
                hasFeedback
            >
                <Input.Password placeholder="Şifre" prefix={<LockOutlined/>} size={"large"}/>
            </Form.Item>

            <Form.Item>
                <Button
                    type="primary"
                    htmlType="submit"
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
    )
}

export default LoginForm