import React, {useEffect, useState} from "react";
import {useRouter} from "next/navigation";
import Cookies from "universal-cookie";
import {Button, message, notification, Steps, theme} from "antd";
import PhotoUploader from "@/components/photoUploader";
import CreateAddressForm from "@/components/createAddressForm";
import CreateAdvertForm from "@/components/createAdvertForm";
import {findByUsername} from "@/services/user";
import {decodeJwtToken} from "@/libs/auth";

type CreateAdvertStepsProps = {
    setIsOpen: React.Dispatch<React.SetStateAction<boolean>>
}

const CreateAdvertSteps: React.FC<CreateAdvertStepsProps> = ({ setIsOpen }) => {
    const [photoIds, setPhotoIds] = useState<string[]>([]);
    const [addressId, setAddressId] = useState<number>(0);
    const [userId, setUserId] = useState<number>(0)
    const [current, setCurrent] = useState(0);
    const [api, contextHolder] = notification.useNotification();
    const cookies = new Cookies();
    const router = useRouter();

    useEffect(() => {
        const fetchUserId = async () => {
            const accessToken = cookies.get("jwt-access-token");
            const decodeToken = decodeJwtToken(accessToken)

            if (decodeToken !== null) {
                try {
                    if (decodeToken.sub !== undefined) {
                        const response = await findByUsername(decodeToken.sub, accessToken);

                        if (response?.success) {
                            setUserId(response?.data?.id);
                        }
                    }
                } catch (error) {
                    console.error(error);
                }
            }
        }
        fetchUserId()
            .then(resolve => console.log(resolve))
            .catch(reject => console.log(reject))
    }, [])

    const steps  = [
        { key: 'Advert Address', component: <CreateAddressForm setAddressId={setAddressId} /> },
        { key: 'Advert Photos', component: <PhotoUploader setPhotoIds={setPhotoIds}/> },
        { key: 'Advert Details', component: <CreateAdvertForm userId={userId} addressId={addressId} photoIds={photoIds} /> }
    ];

    const items = steps.map(step => ({
        key: step.key,
        title: step.key,
    }));

    const { token } = theme.useToken();
    const CurrentFormComponent = steps.find(step => step.key === items[current].key)?.component;

    const next = () => {
        setCurrent(current + 1);
    };

    const prev = () => {
        setCurrent(current - 1);
    };

    const openNotification = () => {
        api.open({
            message: 'Advertisement Created',
            description: 'The ad has been created successfully. You are directed to the advertisements page...',
            showProgress: true,
            pauseOnHover: false,
            type: "success",
            placement: "top"
        });
    };

    const handleDone = (addressId: number, photoIds: string[]) => {
        if (addressId == 0) {
            message.error("İlan adresini girmelisiniz")
        } else if (photoIds.length == 0) {
            message.error("İlan fotoğraflarını yüklemelisiniz")
        } else {
            window.location.reload()
            setIsOpen(false);
            setCurrent(0);
            setAddressId(0)
            setPhotoIds([])
            openNotification();
        }
    }

    const contentStyle: React.CSSProperties = {
        lineHeight: 'auto',
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        color: token.colorTextTertiary,
        backgroundColor: token.colorFillAlter,
        borderRadius: token.borderRadiusLG,
        border: `1px dashed ${token.colorBorder}`,
        marginTop: 16,
    };

    return (
        <div>
            {contextHolder}
            <Steps current={current} items={items}/>
            <div style={contentStyle}>
                {CurrentFormComponent}
            </div>
            <div style={{marginTop: 24}}>
                {current < steps.length - 1 && (
                    <Button
                        type="primary"
                        onClick={next}
                    >
                        Next
                    </Button>
                )}
                {current === steps.length - 1 && (
                    <Button type="primary" onClick={() => handleDone(addressId, photoIds)}>
                        Done
                    </Button>
                )}
                {current > 0 && (
                    <Button style={{margin: '0 8px'}} onClick={prev}>
                        Previous
                    </Button>
                )}
            </div>
        </div>
    )
}

export default CreateAdvertSteps;