"use client"
import React, {useEffect, useState} from "react";
import Link from "next/link";
import {Button, Card, Col, Row, Space, Typography} from 'antd';
import {PiMapPinLineFill} from "react-icons/pi";
import {FaTurkishLiraSign} from "react-icons/fa6";
import {FaPlus} from "react-icons/fa";
import CreateAdvertModal from "@/components/advertModal";
import Cookies from "universal-cookie";
import {fetchAdverts, IAdvertResponse, IAdvertsApiResponse} from "@/services/advert";

const DashboardContainer: React.FC = () => {
    const [isOpen, setIsOpen] = useState<boolean>(false);
    const [adverts, setAdverts] = useState<IAdvertResponse[]>([]);
    const cookies = new Cookies();

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

    console.log(adverts)

    return (
        <>
            <div style={{
                position: 'relative',
                padding: '20px',
                display: 'flex',
                justifyContent: 'center',
                alignItems: 'center'
            }}>
                <Button
                    style={{
                        position: 'absolute',
                        right: '40px',
                        top: -35
                    }}
                    type="primary"
                    size={"large"}
                    onClick={() => setIsOpen(true)}
                >
                    <FaPlus style={{fontSize: 20}}/>
                    <span style={{marginBottom: 2, fontWeight: "bold"}}>Create Advert</span>
                </Button>
                <div style={{display: 'flex', flexWrap: 'wrap', justifyContent: 'space-around'}}>
                    {adverts.map((advert) => (
                        <Card
                            hoverable
                            style={{width: 240, marginBottom: 30, marginLeft: 20}}
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
                            <Space style={{display: "flex", alignItems: "center", color: "#3fc51f"}}>
                                <Typography.Text style={{color: "#3fc51f", fontWeight: "bold"}}>
                                    {advert.price}
                                </Typography.Text>
                                <FaTurkishLiraSign/>
                            </Space>
                            <Row>
                                <Col span={12}><b>Rooms:&nbsp;</b>{advert.numberOfRooms}</Col>
                                <Col span={12}><b>Area:&nbsp;</b> {advert.area} m&sup2;</Col>
                            </Row>
                            <Link href={`/dashboard/advert/${advert.id}`}>
                                <Button style={{marginTop: 10, width: "100%", fontSize: 16}} type="primary">
                                    Details
                                </Button>
                            </Link>
                        </Card>
                    ))}
                </div>
            </div>
            <CreateAdvertModal isOpen={isOpen} setIsOpen={setIsOpen}/>
        </>
    )
}

export default DashboardContainer;
