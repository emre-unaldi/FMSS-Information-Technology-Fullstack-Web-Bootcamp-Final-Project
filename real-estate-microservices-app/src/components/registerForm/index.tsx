"use client"
import React from "react";
import {useRouter} from "next/navigation";
import {Button, Col, Form, Input, message, Row} from "antd";
import {LockOutlined, MailOutlined, PhoneOutlined, UserOutlined} from "@ant-design/icons";
import {IUserRegisterData, register} from "@/services/user";

type RegisterFormValues = {
    "firstName": "string",
    "lastName": "string",
    "username": "string",
    "email": "string",
    "password": "string",
    "confirmPassword": "string",
    "phoneNumber": "string"
}

const RegisterForm: React.FC = () => {
    const [buttonLoading, setButtonLoading] = React.useState(false);
    const [messageApi, contextHolder] = message.useMessage();
    const key = 'register';
    const router = useRouter();

    const onFinish = async (values: RegisterFormValues) => {
        setButtonLoading(true)
        messageApi.open({
            key,
            type: 'loading',
            content: 'Registering user...',
        });
        const {
            firstName,
            lastName,
            username,
            email,
            password,
            phoneNumber
        } = values;
        const formData: IUserRegisterData = {
            firstName,
            lastName,
            username,
            email,
            password,
            phoneNumber,
            roles: ["user"]
        }

        try {
            const response = await register(formData)

            if (response?.success) {
                setButtonLoading(false)
                setTimeout(() => {
                    messageApi.open({
                        key,
                        type: 'success',
                        content: 'User registration successful. Redirecting...',
                        duration: 2,
                    });
                }, 1000);

                router.push("/login")
            } else {
                setButtonLoading(false)
                setTimeout(() => {
                    messageApi.open({
                        key,
                        type: 'error',
                        content: 'User registration failed. Please try again',
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
                    content: 'User registration failed. Please try again',
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
                name="register"
                onFinish={onFinish}
                onFinishFailed={onFinishFailed}
                layout="vertical"
            >
                <Row gutter={[16, 16]}>
                    <Col span={12}>
                        <Form.Item
                            name="firstName"
                            rules={[{required: true, message: 'Please enter your name!'}]}
                            hasFeedback
                        >
                            <Input placeholder="First name" prefix={<UserOutlined/>} size={"large"}/>
                        </Form.Item>
                    </Col>
                    <Col span={12}>
                        <Form.Item
                            name="lastName"
                            rules={[{required: true, message: 'Please enter your last name!'}]}
                            hasFeedback
                        >
                            <Input placeholder="Last name" prefix={<UserOutlined/>} size={"large"}/>
                        </Form.Item>
                    </Col>
                </Row>

                <Form.Item
                    name="username"
                    rules={[{required: true, message: 'Username required!'}]}
                    hasFeedback
                >
                    <Input placeholder="Username" prefix={<UserOutlined/>} size={"large"}/>
                </Form.Item>

                <Form.Item
                    name="email"
                    rules={[
                        {required: true, message: 'Email address required!'},
                        {type: 'email', message: 'Invalid e-mail address!'}
                    ]}
                    hasFeedback
                >
                    <Input placeholder="Email" prefix={<MailOutlined/>} size={"large"}/>
                </Form.Item>

                <Form.Item
                    name="password"
                    rules={[
                        {required: true, message: 'Password required!'},
                        {min: 8, message: 'Your password must be at least 8 characters'}
                    ]}
                    hasFeedback
                >
                    <Input.Password placeholder="Password" prefix={<LockOutlined/>} size={"large"}/>
                </Form.Item>

                <Form.Item
                    name="confirmPassword"
                    dependencies={['password']}
                    rules={[
                        {required: true, message: 'Password confirmation required!'},
                        ({getFieldValue}) => ({
                            validator(_, value) {
                                if (!value || getFieldValue('password') === value) {
                                    return Promise.resolve();
                                }
                                return Promise.reject(new Error('The new password you entered does not match!'));
                            },
                        }),
                    ]}
                    hasFeedback
                >
                    <Input.Password placeholder="Confirm Password" prefix={<LockOutlined/>} size={"large"}/>
                </Form.Item>

                <Form.Item
                    name="phoneNumber"
                    rules={[
                        {required: true, message: 'Phone number is required!'},
                        {
                            pattern: /^\+?\d{1,3}[\s.-]?\(?\d{3}\)?[\s.-]?\d{3}[\s.-]?\d{4}$/,
                            message: 'Phone number format is not correct!'
                        }
                    ]}
                    hasFeedback
                >
                    <Input placeholder="Phone number" prefix={<PhoneOutlined/>} size={"large"}/>
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
                        Register
                    </Button>
                </Form.Item>
            </Form>
        </>
    )
}

export default RegisterForm;