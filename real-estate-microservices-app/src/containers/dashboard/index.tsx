"use client"
import React from "react";
import Link from "next/link";
import {Button, Card, Col, Row, Space, Typography} from 'antd';
import {PiMapPinLineFill} from "react-icons/pi";
import {FaTurkishLiraSign} from "react-icons/fa6";

const DashboardContainer: React.FC = () => {

    return (
        <div style={{display: 'flex', flexWrap: 'wrap', justifyContent: 'space-around'}}>
            {[...Array(25)].map((_, index) => (
                <Card
                    hoverable
                    style={{width: 240, marginBottom: 30}}
                    cover={<img alt={`example${index}`} src="https://via.placeholder.com/150"/>}
                    key={index}
                >
                    <Space>
                        <Typography.Title level={5}>
                            Modern apartment in the city center
                        </Typography.Title>
                    </Space>
                    <Space style={{display: "flex", alignItems: "center"}}>
                        <PiMapPinLineFill style={{color: "red", marginTop: 6}}/>
                        <Typography.Text>
                            Kayseri / İncesu
                        </Typography.Text>
                    </Space>
                    <Space style={{display: "flex", alignItems: "center", color: "#3fc51f"}}>
                        <Typography.Text style={{color: "#3fc51f", fontWeight: "bold"}}>
                            1.300.000
                        </Typography.Text>
                        <FaTurkishLiraSign/>
                    </Space>
                    <Row>
                        <Col span={12}><b>Rooms:</b> 9</Col>
                        <Col span={12}><b>Area:</b> 450 m&sup2;</Col>
                    </Row>
                    <Link href={`/dashboard/advert/${index}`}>
                        <Button style={{marginTop: 10, width: "100%", fontSize: 16}} type="primary">
                            Details
                        </Button>
                    </Link>
                </Card>
            ))}
        </div>
    )
}

export default DashboardContainer;