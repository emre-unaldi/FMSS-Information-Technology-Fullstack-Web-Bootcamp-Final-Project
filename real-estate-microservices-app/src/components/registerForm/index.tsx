"use client"
import React from "react";
import {Button, Col, Form, Input, Row} from "antd";
import {LockOutlined, MailOutlined, PhoneOutlined, UserOutlined} from "@ant-design/icons";
import {useRouter} from "next/navigation";

type RegisterFormValues = {
    "firstName": "string",
    "lastName": "string",
    "username": "string",
    "email": "string",
    "password": "string",
    "phoneNumber": "string"
}

const RegisterForm: React.FC = () => {

    const router = useRouter();

    const onFinish = (values: RegisterFormValues) => {
        console.log('Success:', values);
        alert(JSON.stringify(values));
        router.push("/login")
    };

    const onFinishFailed = (errorInfo: any) => {
        console.log('Failed:', errorInfo);
    };

    return (
        <Form
            name="register"
            initialValues={{ }}
            onFinish={onFinish}
            onFinishFailed={onFinishFailed}
            layout="vertical"
        >
            <Row gutter={[16, 16]}>
                <Col span={12}>
                    <Form.Item
                        name="firstName"
                        rules={[{ required: true, message: 'Lütfen adınızı giriniz!' }]}
                        hasFeedback
                    >
                        <Input placeholder="Adınız" prefix={<UserOutlined />} size={"large"} />
                    </Form.Item>
                </Col>
                <Col span={12}>
                    <Form.Item
                        name="lastName"
                        rules={[{ required: true, message: 'Lütfen soyadınızı giriniz!' }]}
                        hasFeedback
                    >
                        <Input placeholder="Soyadınız" prefix={<UserOutlined />} size={"large"} />
                    </Form.Item>
                </Col>
            </Row>

            <Form.Item
                name="username"
                rules={[{ required: true, message: 'Kullanıcı adı gereklidir!' }]}
                hasFeedback
            >
                <Input placeholder="Kullanıcı Adı" prefix={<UserOutlined />} size={"large"} />
            </Form.Item>

            <Form.Item
                name="email"
                rules={[
                    { required: true, message: 'E-posta adresi gereklidir!' },
                    { type: 'email', message: 'Geçersiz e-posta adresi!' }
                ]}
                hasFeedback
            >
                <Input placeholder="E-posta" prefix={<MailOutlined />} size={"large"} />
            </Form.Item>

            <Form.Item
                name="password"
                rules={[
                    { required: true, message: 'Şifre gereklidir!' },
                    { min: 8, message: 'Şifreniz en az 8 karakter olmalıdır.' }
                ]}
                hasFeedback
            >
                <Input.Password placeholder="Şifre" prefix={<LockOutlined />} size={"large"} />
            </Form.Item>

            <Form.Item
                name="confirmPassword"
                dependencies={['password']}
                rules={[
                    { required: true, message: 'Şifre onayı gereklidir!' },
                    ({ getFieldValue }) => ({
                        validator(_, value) {
                            if (!value || getFieldValue('password') === value) {
                                return Promise.resolve();
                            }
                            return Promise.reject(new Error('Girdiğiniz yeni şifre eşleşmiyor!'));
                        },
                    }),
                ]}
                hasFeedback
            >
                <Input.Password placeholder="Şifre Onayı" prefix={<LockOutlined />} size={"large"} />
            </Form.Item>

            <Form.Item
                name="phoneNumber"
                rules={[{ required: true, message: 'Telefon numarası gereklidir!' }]}
                hasFeedback
            >
                <Input placeholder="Telefon Numarası" prefix={<PhoneOutlined />} size={"large"} />
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
                    Kayıt Ol
                </Button>
            </Form.Item>
        </Form>
    )
}

export default RegisterForm;