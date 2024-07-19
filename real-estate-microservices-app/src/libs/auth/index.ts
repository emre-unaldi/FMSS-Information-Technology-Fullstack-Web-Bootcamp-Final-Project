import { jwtVerify } from "jose";

const getJwtSecretKey = () => {
    const jwtSecretKey: string | undefined = process.env.NEXT_PUBLIC_JWT_SECRET_KEY;

    if(!jwtSecretKey) {
        throw new Error('JWT secret key is not available');
    }

    return new TextEncoder().encode(jwtSecretKey);
}

const verifyJwtToken = async (accessToken: string) => {
    try {
        const { payload } = await jwtVerify(accessToken, getJwtSecretKey());
        console.log("payload : " + payload);
        return payload;
    } catch (error) {
        console.log("Verify JWT Token : " + error);
        return null;
    }
}

export { verifyJwtToken, getJwtSecretKey }