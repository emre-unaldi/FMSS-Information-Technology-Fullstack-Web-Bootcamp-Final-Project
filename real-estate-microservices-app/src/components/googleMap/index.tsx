"use client"
import React, {useState} from "react";
import {APIProvider, Map, AdvancedMarker, Pin, InfoWindow} from "@vis.gl/react-google-maps";

type GoogleMapProps = {
    location: {
        lat: number,
        lng: number
    },
    address: string
}

const GoogleMap: React.FC<GoogleMapProps> = (props) => {
    const [open, setOpen] = useState<boolean>(false)
    const {location: position, address} = props

    const apiKey: string = process.env.NEXT_PUBLIC_GOOGLE_API_KEY ?? "";
    const mapId: string = process.env.NEXT_PUBLIC_GOOGLE_MAP_ID ?? "";

    return (
        <APIProvider apiKey={apiKey}>
            <div style={{height: "50vh", width: "100%", border: "2px solid gray"}}>
                <Map defaultZoom={9} defaultCenter={position} mapId={mapId}>
                    <AdvancedMarker position={position} onClick={() => setOpen(true)}>
                        <Pin background={"red"} borderColor={"gray"} glyphColor={"white"}/>
                    </AdvancedMarker>
                    {open && (
                        <InfoWindow
                            position={position}
                            onCloseClick={() =>
                                setOpen(false)}
                        >
                            <p>{address}</p>
                        </InfoWindow>
                    )}
                </Map>
            </div>
        </APIProvider>
    )
}

export default GoogleMap;