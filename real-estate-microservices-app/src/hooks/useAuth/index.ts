import React from "react";
import { verifyJwtToken } from "@/libs/auth";
import Cookies from "universal-cookie";
import { JWTPayload } from "jose";

const fromServer = async () => {
    const cookies = require("next/headers").cookies;
    const cookieList = cookies();

    const { value: accessToken } = cookieList.get("jwt-access-token") ?? { value: null }
    const verifiedToken = await verifyJwtToken(accessToken);

    console.log("verifiedToken : " + JSON.stringify(verifiedToken));

    return verifiedToken;
}

export const useAuth = () => {
    const [auth, setAuth] = React.useState<JWTPayload | null>(null);

    const getVerifiedToken = async () => {
        const cookies = new Cookies();
        const accessToken = cookies.get("jwt-access-token") ?? null;
        const verifiedToken = await verifyJwtToken(accessToken);

        console.log("verifiedToken : " + JSON.stringify(verifiedToken));

        setAuth(verifiedToken);
    }

    React.useEffect(() => {
        getVerifiedToken()
            .then(resolve => console.log(resolve))
            .catch(reject => console.log(reject))
    }, [])

    return auth;
}

useAuth.fromServer = fromServer;