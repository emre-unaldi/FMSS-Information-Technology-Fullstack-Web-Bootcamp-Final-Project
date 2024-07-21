import React from 'react';
import Cookies from "universal-cookie";
import {Form, Input, Button, Card, Divider, message} from 'antd';
import {createAddress, IAddress} from "@/services/address";

type CreateAddressFormProps = {
    setAddressId: React.Dispatch<React.SetStateAction<number>>;
}

const CreateAddressForm: React.FC<CreateAddressFormProps> = ({ setAddressId }) => {
    const [messageApi, contextHolder] = message.useMessage();
    const key = 'address';
    const cookies = new Cookies();

    const onFinish = async (values: IAddress) => {
        messageApi.open({
            key,
            type: 'loading',
            content: 'Address is being saved...',
        });

        const accessToken = await cookies.get("jwt-access-token");
        const { neighborhood, street, province, county, zipCode  } = values;
        const addressData: IAddress = { neighborhood, street, province, county, zipCode  };

        try {
            const response = await createAddress(addressData, accessToken);
            console.log(response)

            if (response?.success) {
                setTimeout(() => {
                    messageApi.open({
                        key,
                        type: 'success',
                        content: 'Address saved successfully',
                        duration: 2
                    });
                }, 1000);
                console.log(response)
                setAddressId(response?.data?.id)
            } else {
                setTimeout(() => {
                    messageApi.open({
                        key,
                        type: 'error',
                        content: 'Address save failed',
                        duration: 2
                    });
                }, 1000);
            }
        } catch (error) {
            setTimeout(() => {
                messageApi.open({
                    key,
                    type: 'error',
                    content: 'An error occurred while saving the address',
                    duration: 2
                });
            }, 1000);
        }

    };

    const onFinishFailed = (errorInfo: any) => {
        console.log('Failed:', errorInfo);
    };

    return (
        <>
            {contextHolder}
            <Card style={{ width: '50%', margin: "20px auto"}} >
                <Form
                    name="address"
                    onFinish={onFinish}
                    onFinishFailed={onFinishFailed}
                    initialValues={{
                        neighborhood: '',
                        street: '',
                        province: '',
                        county: '',
                        zipCode: '',
                    }}
                    style={{
                        width: "100%"
                    }}
                >
                    <Form.Item
                        name="neighborhood"
                        rules={[{ required: true, message: 'Neighborhood required!' }]}
                        hasFeedback
                    >
                        <Input placeholder="Neighborhood" />
                    </Form.Item>

                    <Form.Item
                        name="street"
                        rules={[{ required: true, message: 'Street required!' }]}
                        hasFeedback
                    >
                        <Input placeholder="Street" />
                    </Form.Item>

                    <Form.Item
                        name="province"
                        rules={[{ required: true, message: 'Province required!' }]}
                        hasFeedback
                    >
                        <Input placeholder="Province" />
                    </Form.Item>

                    <Form.Item
                        name="county"
                        rules={[{ required: true, message: 'County required!' }]}
                        hasFeedback
                    >
                        <Input placeholder="County" />
                    </Form.Item>

                    <Form.Item
                        name="zipCode"
                        rules={[{ required: true, message: 'Zip Code required!' }]}
                        hasFeedback
                    >
                        <Input placeholder="Zip Code" />
                    </Form.Item>

                    <Divider />

                    <Form.Item>
                        <Button
                            type="primary"
                            htmlType="submit"
                            size={"large"}
                            style={{ fontSize: 18, width: "100%" }}
                        >
                            Save Address
                        </Button>
                    </Form.Item>
                </Form>
            </Card>
        </>
    );
};

export default CreateAddressForm;
