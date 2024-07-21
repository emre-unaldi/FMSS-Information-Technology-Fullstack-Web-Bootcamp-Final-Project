import React from "react";
import AdvertDetailsContainer from "@/containers/advert";

type AdvertDetailsProps = {
    params: {
        id: string
    }
}

const AdvertDetails: React.FC<AdvertDetailsProps> = ({params}) => {
    const {id} = params

    return <AdvertDetailsContainer id={id}/>
}

export default AdvertDetails