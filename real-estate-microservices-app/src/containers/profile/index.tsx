"use client"
import React, {useEffect, useState} from "react";
import Link from "next/link";
import {Avatar, Button, Card, Carousel, Col, Descriptions, message, Modal, Row, Space, Typography} from "antd";
import {ExclamationCircleFilled, UserOutlined} from "@ant-design/icons";
import Cookies from "universal-cookie";
import {decodeJwtToken} from "@/libs/auth";
import {findByUsername} from "@/services/user";
import {IUser} from "@/services/user";
import {deleteAdvert, fetchAdverts, IAdvertResponse, IAdvertsApiResponse} from "@/services/advert";
import {PiMapPinLineFill} from "react-icons/pi";
import {FaTurkishLiraSign} from "react-icons/fa6";

const ProfileContainer: React.FC = () => {
    const [user, setUser] = useState<IUser>();
    const [adverts, setAdverts] = useState<IAdvertResponse[]>([]);
    const cookies = new Cookies();
    const { confirm } = Modal;
    const [messageApi, contextHolder] = message.useMessage();
    const key = 'advert_delete';

    useEffect(() => {
        const fetchUser = async () => {
            const accessToken = cookies.get("jwt-access-token");
            const decodeToken = decodeJwtToken(accessToken)

            if (decodeToken !== null) {
                try {
                    if (decodeToken.sub !== undefined) {
                        const response = await findByUsername(decodeToken.sub, accessToken);

                        if (response?.success) {
                            setUser(response?.data);
                        } else {
                            console.log("")
                        }
                    }
                } catch (error) {
                    console.error(error);
                }
            }
        }

        fetchUser()
            .then(resolve => console.log(resolve))
            .catch(reject => console.log(reject))
    }, []);

    useEffect(() => {
        const getAdverts = async () => {
            const accessToken = await cookies.get("jwt-access-token");
            const response: IAdvertsApiResponse = await fetchAdverts(accessToken);

            if (response?.success) {
                setAdverts(response?.data)
            } else {
                throw new Error("Could not fetch adverts response");
            }
        }

        getAdverts()
            .then(resolve => console.log(resolve))
            .catch(reject => console.log(reject))
    }, []);

    const handleAdvertDelete = (advertId: number): Promise<void> => {
        return new Promise(async (resolve, reject) => {
            const key = 'deletingAdvert';
            messageApi.open({
                key,
                type: 'loading',
                content: 'Advert is being deleted...',
            });

            const accessToken = cookies.get("jwt-access-token");

            const response = await deleteAdvert(advertId, accessToken);

            if (response?.success) {
                setTimeout(() => {
                    messageApi.open({
                        key,
                        type: 'success',
                        content: 'Advert deleted successfully',
                        duration: 2
                    });
                    resolve();
                }, 2000);
            } else {
                setTimeout(() => {
                    messageApi.open({
                        key,
                        type: 'error',
                        content: 'Advert delete failed',
                        duration: 2
                    });
                    reject();
                }, 2000);
            }
        });
    };

    const showDeleteConfirm = (advertId: number) => {
        confirm({
            title: 'Do you want to delete these advert?',
            icon: <ExclamationCircleFilled />,
            content: 'This advert record will be deleted when the OK button is pressed',
            async onOk() {
                try {
                    await handleAdvertDelete(advertId);
                    return Promise.resolve();
                } catch {
                    console.log('Advert delete confirm error');
                    return Promise.reject();
                }
            },
        });
    };

    return (
        <div>
            {contextHolder}
            <Row justify="center" align="top" style={{height: '50%', width: '100%'}}>
                <Col span={4}>
                    <Card style={{
                        display: 'flex',
                        justifyContent: "center",
                        alignItems: "center",
                        marginBottom: "20px",
                        marginRight: "20px"
                    }}
                    >
                        <Avatar
                            style={{margin: "62px auto"}}
                            size={150}
                            icon={<UserOutlined/>}
                        />
                    </Card>
                </Col>
                <Col span={20}>
                    <Card>
                        <Descriptions bordered title="User information" style={{marginBottom: "20px"}}>
                            <Descriptions.Item label="First Name">{user?.firstName}</Descriptions.Item>
                            <Descriptions.Item label="Last Name">{user?.firstName}</Descriptions.Item>
                            <Descriptions.Item label="Username">{user?.username}</Descriptions.Item>
                            <Descriptions.Item label="Email">{user?.email}</Descriptions.Item>
                            <Descriptions.Item label="Phone Number">{user?.phoneNumber}</Descriptions.Item>
                            <Descriptions.Item
                                label="Subscription Status">{user?.account?.isSubscribe ? 'Subscriber' : 'Non-Subscriber'}</Descriptions.Item>
                        </Descriptions>
                        <Descriptions bordered title="Account Details">
                            <Descriptions.Item label="Number of Advert">{user?.account.advertCount}</Descriptions.Item>
                            <Descriptions.Item
                                label="Subscription Validity Date">{user?.account?.expirationDate}</Descriptions.Item>
                            <Descriptions.Item
                                label="Subscription Status">{user?.account?.isSubscribe ? 'Subscriber' : 'Non-Subscriber'}</Descriptions.Item>
                        </Descriptions>
                    </Card>
                </Col>
            </Row>
            <Row>
                <Col span={24}  >
                    <Card>
                        <div style={{display: 'flex', flexWrap: 'wrap', justifyContent: 'space-around'}}>
                            {adverts
                                .filter(advert => advert?.user?.id == user?.id)
                                .map(advert => (
                                    <Card
                                        hoverable
                                        style={{
                                            width: 240,
                                            marginBottom: 30,
                                            margin: "10px auto"
                                    }}
                                        cover={
                                            <img
                                                alt={`example${advert.id}`}
                                                src={advert.photos[0].downloadUrl}
                                                height={150}
                                            />}
                                        key={advert.id}
                                    >
                                        <Space>
                                            <Typography.Title level={5}>
                                                {advert.title}
                                            </Typography.Title>
                                        </Space>
                                        <Space style={{display: "flex", alignItems: "center"}}>
                                            <PiMapPinLineFill style={{color: "red", marginTop: 6}}/>
                                            <Typography.Text>
                                                {`${advert.address.province} / ${advert.address.county}`}
                                            </Typography.Text>
                                        </Space>
                                        <Space
                                            style={{display: "flex", alignItems: "center", color: "#3fc51f"}}>
                                            <Typography.Text style={{color: "#3fc51f", fontWeight: "bold"}}>
                                                {advert.price}
                                            </Typography.Text>
                                            <FaTurkishLiraSign/>
                                        </Space>
                                        <Row>
                                            <Col span={12}><b>Rooms:&nbsp;</b>{advert.numberOfRooms}</Col>
                                            <Col span={12}><b>Area:&nbsp;</b> {advert.area} m&sup2;</Col>
                                        </Row>
                                        <Row style={{ display: "flex", justifyContent: "space-around", alignItems: "center" }} >
                                            <Col span={10}>
                                                <Link href={`/dashboard/advert/${advert.id}`}>
                                                    <Button
                                                        style={{marginTop: 10, width: "100%", fontSize: 16}}
                                                        type="primary"
                                                    >
                                                        Details
                                                    </Button>
                                                </Link>
                                            </Col>
                                            <Col span={10} style={{ marginLeft: 10 }} >
                                                <Button
                                                    style={{marginTop: 10, width: "100%", fontSize: 16, backgroundColor: "red"}}
                                                    type="primary"
                                                    onClick={() => showDeleteConfirm(advert.id)}
                                                >
                                                    Delete
                                                </Button>
                                            </Col>
                                        </Row>
                                    </Card>
                                ))}
                        </div>

                    </Card>
                </Col>
            </Row>
        </div>
    );
};

export default ProfileContainer;
