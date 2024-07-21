import React from 'react';
import Cookies from "universal-cookie";
import {Form, Input, Button, Select, DatePicker, Card, Row, Col, Flex, Switch, Divider, message} from 'antd';
import { RuleObject } from 'antd/lib/form';
import dayjs from 'dayjs';
import {createAdvert, IAdvert} from "@/services/advert";

const { Option } = Select;

type AdvertFormValues = {
    housingType: string;
    advertType: string;
    title: string;
    description: string;
    validityDate: dayjs.Dayjs;
    area: number;
    numberOfRooms: number;
    price: number;
    isBalcony: boolean;
    isCarPark: boolean;
};

type CreateAdvertFormProps = {
    userId: number,
    addressId: number,
    photoIds: string[]
}

const CreateAdvertForm: React.FC<CreateAdvertFormProps> = ({userId,addressId, photoIds}) => {
    const [messageApi, contextHolder] = message.useMessage();
    const key = 'advert';
    const cookies = new Cookies();

    const onFinish = async (values: AdvertFormValues) => {
        messageApi.open({
            key,
            type: 'loading',
            content: 'Advert is being saved...',
        });

        const accessToken = await cookies.get('jwt-access-token');

        const {
            title,
            description,
            housingType,
            advertType,
            validityDate,
            area,
            numberOfRooms,
            price,
            isBalcony,
            isCarPark
        } = values

        const formattedValidityDate = convertDateTime(values.validityDate)
        const formattedReleaseDate = convertDateTime(dayjs())

        const advertData: IAdvert = {
            photoIds,
            userId,
            housingType,
            advertType,
            title,
            description,
            addressId,
            releaseDate: formattedReleaseDate,
            validityDate: formattedValidityDate,
            area,
            numberOfRooms,
            price,
            isBalcony,
            isCarPark
        }

        try {
            const response = await createAdvert(advertData, accessToken);
            console.log(response)

            if (response?.success) {
                setTimeout(() => {
                    messageApi.open({
                        key,
                        type: 'success',
                        content: 'Advert saved successfully',
                        duration: 2
                    });
                }, 1000);
            } else {
                setTimeout(() => {
                    messageApi.open({
                        key,
                        type: 'error',
                        content: 'Advert save failed',
                        duration: 2
                    });
                }, 1000);
            }
        } catch (error) {
            setTimeout(() => {
                messageApi.open({
                    key,
                    type: 'error',
                    content: 'An error occurred while saving the advert',
                    duration: 2
                });
            }, 1000);
        }
    };

    const convertDateTime = (dateTime: dayjs.Dayjs) => {
        const date = dayjs(dateTime)
            .endOf('day')
            .toISOString()
            .split("T")

        return date[0];
    }

    const onFinishFailed = (errorInfo: any) => {
        console.log('Failed:', errorInfo);
    };

    const validateNumber = (min: number, message: string) => ({
        validator(_: RuleObject, value: number) {
            if (!value || value > min) {
                return Promise.resolve();
            }
            return Promise.reject(new Error(message));
        },
    });

    return (
        <>
            {contextHolder}
            <Card style={{ width: '50%', margin: "20px auto"}} >
                <Form
                    name="advert"
                    onFinish={onFinish}
                    onFinishFailed={onFinishFailed}
                    initialValues={{
                        housingType: '',
                        advertStatus: '',
                        advertType: '',
                        title: '',
                        description: '',
                        releaseDate: null,
                        validityDate: null,
                        area: '',
                        numberOfRooms: '',
                        price: '',
                        isBalcony: false,
                        isCarPark: false,
                    }}
                    style={{
                        width: "100%"
                    }}
                >
                    <Form.Item
                        name="title"
                        rules={[{ required: true, message: 'Title is required!' }]}
                        hasFeedback
                    >
                        <Input placeholder="Enter the title" />
                    </Form.Item>

                    <Form.Item
                        name="description"
                        rules={[{ required: true, message: 'Description is required!' }]}
                        hasFeedback
                    >
                        <Input.TextArea placeholder="Enter the description" />
                    </Form.Item>

                    <Form.Item
                        name="housingType"
                        rules={[{ required: true, message: 'Housing type is required!' }]}
                        hasFeedback
                    >
                        <Select >
                            <Option value="APARTMENT">Apartment</Option>
                            <Option value="RESIDENCE">Residence</Option>
                            <Option value="DETACHED_HOUSE">Detached House</Option>
                            <Option value="VILLA">Villa</Option>
                            <Option value="SUMMER_HOUSE">Summer House</Option>
                        </Select>
                    </Form.Item>

                    <Form.Item
                        name="advertType"
                        rules={[{ required: true, message: 'Advert type is required!' }]}
                        hasFeedback
                    >
                        <Select>
                            <Option value="FOR_SENT">For Sent</Option>
                            <Option value="FOR_SALE">For Sale</Option>
                        </Select>
                    </Form.Item>

                    <Form.Item
                        name="validityDate"
                        rules={[{ required: true, message: 'Validity date is required!' }]}
                        hasFeedback
                    >
                        <DatePicker
                            format="YYYY-MM-DD"
                            disabledDate={(current) => current && current < dayjs().endOf('day')}
                            style={{ width: '100%' }}
                        />
                    </Form.Item>

                    <Form.Item
                        name="area"
                        rules={[
                            { required: true, message: 'Area (m²) is required!' },
                            validateNumber(0, 'Area must be greater than 0'),
                        ]}
                        hasFeedback
                    >
                        <Input type="number" placeholder="Enter the area" />
                    </Form.Item>

                    <Form.Item
                        name="numberOfRooms"
                        rules={[
                            { required: true, message: 'Number of rooms is required!' },
                            validateNumber(0, 'Number of rooms must be greater than 0'),
                        ]}
                        hasFeedback
                    >
                        <Input type="number" placeholder="Enter the number of rooms" />
                    </Form.Item>

                    <Form.Item
                        name="price"
                        rules={[
                            { required: true, message: 'Price is required!' },
                            validateNumber(0, 'Price must be greater than 0'),
                        ]}
                        hasFeedback
                    >
                        <Input type="number" placeholder="Enter the price" />
                    </Form.Item>

                    <Row>
                        <Flex align={"center"} justify={"space-around"} style={{ width: "100%" }} >
                            <Col span={12}>
                                <Form.Item
                                    name="isBalcony"
                                    valuePropName="checked"
                                    style={{ marginLeft: 70 }}
                                    hasFeedback
                                    label={"Balcony"}
                                >
                                    <Switch />
                                </Form.Item>
                            </Col>
                            <Col span={12}>
                                <Form.Item
                                    name="isCarPark"
                                    valuePropName="checked"
                                    style={{ marginLeft: 70 }}
                                    hasFeedback
                                    label={"Car Park"}
                                >
                                    <Switch />
                                </Form.Item>
                            </Col>
                        </Flex>
                    </Row>

                    <Divider />

                    <Form.Item>
                        <Button
                            type="primary"
                            htmlType="submit"
                            size={"large"}
                            style={{ fontSize: 18, width: "100%" }}
                        >
                            Save Advert
                        </Button>
                    </Form.Item>
                </Form>
            </Card>
        </>
    );
};

export default CreateAdvertForm;
