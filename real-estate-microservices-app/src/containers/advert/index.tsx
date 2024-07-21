"use client"
import React, {useEffect, useState} from "react";
import Image from "next/image";
import {Badge, Card, Carousel, Col, Descriptions, DescriptionsProps, Divider, Row, Tag} from "antd";
import {getLocation, ILocation} from "@/services/google"
import GoogleMap from "@/components/googleMap";
import { FcOk } from "react-icons/fc";
import { IoIosCloseCircle } from "react-icons/io";
import { FaTurkishLiraSign } from "react-icons/fa6";
import {fetchAdvert, IAddress, IAdvertResponse, IAdvertsApiResponse} from "@/services/advert";
import Cookies from "universal-cookie";

type AdvertDetailsContainerProps = {
    id: string
}

const AdvertDetailsContainer: React.FC<AdvertDetailsContainerProps> = ({ id }) => {
    const [location, setLocation] = React.useState<ILocation>();
    const [advert, setAdvert] = useState<IAdvertResponse>();
    const [advertAddress, setAdvertAddress] = React.useState<string>("");
    const cookies = new Cookies();

    useEffect( () => {
        const getAdvert = async () => {
            const accessToken = await cookies.get("jwt-access-token");
            const advertId = parseInt(id);
            const response = await fetchAdvert(advertId, accessToken);

            if (response?.success) {
                setAdvert(response?.data)
                const address: IAddress = response?.data?.address
                const mapAddress: string = `
                    ${address.neighborhood} 
                    ${address.street} 
                    ${address.province} 
                    ${address.county} 
                    ${address.zipCode}
                `
                const location = await getLocation(mapAddress)
                setLocation(location)
                setAdvertAddress(mapAddress);
            } else {
                throw new Error("Could not fetch adverts response");
            }
        }

        getAdvert()
            .then(resolve => console.log(resolve))
            .catch(reject => console.log(reject))
    }, [id]);

    const checkAdvertStatus = (status: string | undefined) => {
        switch (status) {
            case "ACTIVE":
                    return (<Badge status="success" text="Active" />)
            case "PASSIVE":
                    return (<Badge status="warning" text="Passive" />)
            case "IN_REVIEW":
                    return (<Badge status="processing" text="In Review" />)
            default:
                return (<Badge status="processing" text="In Review" />)
        }
    }

    const items: DescriptionsProps['items'] = [
        {
            key: '1',
            label: (<b>Advert Number</b>),
            children: (<span>{advert?.advertNumber}</span>),
            span: 3,
        },
        {
            key: '2',
            label: (<b>Housing Type</b>),
            children: (<Tag color={"success"} >{advert?.housingType}</Tag>),
            span: 2,
        },
        {
            key: '3',
            label: (<b>Advert Type</b>),
            children: (<Tag color={"geekblue"} >{advert?.advertType}</Tag>),
            span: 2,
        },
        {
            key: '4',
            label: (<b>Title</b>),
            children: (<span>{advert?.title}</span>),
            span: 3,
        },
        {
            key: '5',
            label: (<b>Description</b>),
            children: (<span>{advert?.description}</span>),
            span: 3,
        },
        {
            key: '6',
            label: (<b>Advert Status</b>),
            span: 2,
            children: checkAdvertStatus(advert?.advertStatus),
        },
        {
            key: '7',
            label: (<b>Release Date</b>),
            children: (<span>{advert?.releaseDate}</span>),
            span: 2,
        },
        {
            key: '8',
            label: (<b>Validity Date</b>),
            children: (<span>{advert?.validityDate}</span>),
            span: 2,
        },
        {
            key: '9',
            label: (<b>Area</b>),
            children: (<span>{advert?.area}</span>),
            span: 2,
        },
        {
            key: '10',
            label: (<b>Number Of Rooms</b>),
            children: (<span>{advert?.numberOfRooms}</span>),
            span: 2,
        },
        {
            key: '11',
            label: (<b>Price</b>),
            children: (<span>{advert?.price} <FaTurkishLiraSign /></span>),
            span: 2,
        },
        {
            key: '12',
            label: (<b>Is Balcony</b>),
            children: (<span>{advert?.isBalcony ? <FcOk style={{fontSize: 20 }} /> : <IoIosCloseCircle style={{fontSize: 22, color: "red"}} />}</span>),
            span: 2,
        },
        {
            key: '13',
            label: (<b>Is Car Park</b>),
            children: (<span>{advert?.isCarPark ? <FcOk style={{fontSize: 20 }} /> : <IoIosCloseCircle style={{fontSize: 22, color: "red"}} />}</span>),
            span: 2
        },
    ];

    return (
        <>
            <Row justify="center" align="top" style={{height: '50%', width: '100%'}}>
                <Col span={12}>
                    <Carousel autoplay>
                        {
                            advert?.photos.map((photo) => (
                                <div style={{ display: 'flex', justifyContent: "center", alignItems: "center" }}  >
                                    <img
                                        src={photo.downloadUrl}
                                        alt={`Image ${id}`}
                                        width={"100%"}
                                        height={420}
                                        key={photo.id}
                                    />
                                </div>
                            ))
                        }
                    </Carousel>
                </Col>
                <Col span={11} style={{marginLeft: 20}}>
                    <Card title={advert?.title}>
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
                    {location && <GoogleMap location={location} address={advertAddress} />}
                </Col>
            </Row>
        </>
    )
}

export default AdvertDetailsContainer;