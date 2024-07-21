import {verifyToken} from "@/services/auth";
import {jwtDecode} from "jwt-decode";

const verifyJwtToken = async (accessToken: string) => {
    try {
        const response = await verifyToken(accessToken);
        console.log("Verify Payload : " + JSON.stringify(response));

        return response;
    } catch (error) {
        console.log("Verify JWT Token Error : " + error);
        return null;
    }
}

const decodeJwtToken = (accessToken: string) => {
    try {
        const payload = jwtDecode(accessToken)
        console.log("Decode Payload : " + JSON.stringify(payload));

        if (payload.sub != null) {
            return payload
        } else {
            return null;
        }
    } catch (error) {
        console.log("Decode JWT Token Error : " + error);
        return null;
    }
}

export { verifyJwtToken, decodeJwtToken }