import {verifyToken} from "@/services/auth";

const verifyJwtToken = async (accessToken: string) => {
    try {
        const response = await verifyToken(accessToken);
        console.log("payload : " + JSON.stringify(response));

        return response;
    } catch (error) {
        console.log("Verify JWT Token : " + error);
        return null;
    }
}

export { verifyJwtToken }