"use client"
import React, {useEffect} from "react";
import {Badge, Card, Carousel, Col, Descriptions, DescriptionsProps, Divider, Row, Tag} from "antd";
import Image from "next/image";
import {getLocation, Location} from "@/services/google"
import GoogleMap from "@/components/googleMap";
import { FcOk } from "react-icons/fc";
import { IoIosCloseCircle } from "react-icons/io";
import { FaTurkishLiraSign } from "react-icons/fa6";

type AdvertDetailsContainerProps = {
    id: string
}

const AdvertDetailsContainer: React.FC<AdvertDetailsContainerProps> = ({ id }) => {
    const [location, setLocation] = React.useState<Location>();
    const address: string = "Karamustafa paşa mahallesi, pekmez sokak menekşe apartman b Blok no 9 / 12 İncesu / Kayseri"

    useEffect( () => {
        const fetchLocation = async (address: string) => {
            const location = await getLocation(address)
            setLocation(location)
        }

        fetchLocation(address)
            .then(resolve => console.log(resolve))
            .catch(reject => console.log(reject))
    }, []);

    console.log("Location: " + JSON.stringify(location));

    const items: DescriptionsProps['items'] = [
        {
            key: '1',
            label: (<b>Advert Number</b>),
            children: 'e52c3da2-8c1a-4096-b534-2f59199628e4',
            span: 3,
        },
        {
            key: '2',
            label: (<b>Housing Type</b>),
            children: (<Tag color={"success"} >VILLA</Tag>),
            span: 2,
        },
        {
            key: '3',
            label: (<b>Advert Type</b>),
            children: (<Tag color={"geekblue"} >For Sent</Tag>),
            span: 2,
        },
        {
            key: '4',
            label: (<b>Title</b>),
            children: 'Sample Apartment Listing 38',
            span: 3,
        },
        {
            key: '5',
            label: (<b>Description</b>),
            children: 'Spacious apartment with great amenities 38',
            span: 3,
        },
        {
            key: '6',
            label: (<b>Advert Status</b>),
            span: 2,
            children: <Badge status="success" text="Active" />,
        },
        {
            key: '7',
            label: (<b>Release Date</b>),
            children: '2024-07-21',
            span: 2,
        },
        {
            key: '8',
            label: (<b>Validity Date</b>),
            children: '2024-12-12',
            span: 2,
        },
        {
            key: '9',
            label: (<b>Area</b>),
            children: '450',
            span: 2,
        },
        {
            key: '10',
            label: (<b>Number Of Rooms</b>),
            children: '6',
            span: 2,
        },
        {
            key: '11',
            label: (<b>Price</b>),
            children: (<span>1,200,000 <FaTurkishLiraSign /></span>),
            span: 2,
        },
        {
            key: '12',
            label: (<b>Is Balcony</b>),
            children: (<span>{true ? <FcOk style={{fontSize: 20 }} /> : <IoIosCloseCircle style={{fontSize: 22, color: "red"}} />}</span>),
            span: 2,
        },
        {
            key: '13',
            label: (<b>Is Car Park</b>),
            children: (<span>{false ? <FcOk style={{fontSize: 20 }} /> : <IoIosCloseCircle style={{fontSize: 22, color: "red"}} />}</span>),
            span: 2
        },
    ];

    return (
        <>
            <Row justify="center" align="top" style={{height: '50%', width: '100%'}}>
                <Col span={12}>
                    <Carousel autoplay>
                        <div style={{display: 'flex', justifyContent: 'center', alignItems: 'center'}}>
                            <Image
                                src={"https://picsum.photos/400/250"}
                                alt="Image 1"
                                layout="responsive"
                                width={400}
                                height={250}
                                objectFit="cover"
                            />
                        </div>
                        <div>
                            <Image
                                src={"https://picsum.photos/400/260"}
                                alt="Image 2"
                                layout="responsive"
                                width={400}
                                height={260}
                                objectFit="cover"
                            />
                        </div>
                    </Carousel>
                </Col>
                <Col span={11} style={{marginLeft: 20}}>
                    <Card title={`Advert Details : ${id}`}>
                        <Descriptions
                            layout={"horizontal"}
                            bordered
                            items={items}
                            size={"small"}
                        />
                    </Card>
                </Col>
            </Row>
            <Divider/>
            <Row justify="center" align="middle" style={{width: '100%', height: '50%'}}>
                <Col span={24}>
                    {location && <GoogleMap location={location} address={address} />}
                </Col>
            </Row>
        </>
    )
}

export default AdvertDetailsContainer;