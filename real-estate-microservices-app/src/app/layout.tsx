import React from "react";
import type {Metadata} from "next";
import Script from "next/script";
import {Inter} from "next/font/google";
import {AntdRegistry} from '@ant-design/nextjs-registry';
import "./globals.css";
import "bootstrap/dist/css/bootstrap.min.css";

const inter = Inter({subsets: ["latin"]});

export const metadata: Metadata = {
    title: "Real Estate App",
    description: "Real Estate Microservices Application"
};

export default function RootLayout({children}: Readonly<{ children: React.ReactNode }>) {
    return (
        <html lang="en">
            <body className={inter.className}>
            <AntdRegistry>
                {children}
            </AntdRegistry>
            <Script
                src={"https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.3.3/js/bootstrap.bundle.min.js"}
                integrity={"sha512-7Pi/otdlbbCR+LnW+F7PwFcSDJOuUJB3OxtEHbg4vSMvzvJjde4Po1v4BR9Gdc9aXNUNFVUY+SK51wWT8WF0Gg=="}
                crossOrigin={"anonymous"}
                referrerPolicy={"no-referrer"}
            />
            </body>
        </html>
    );
}
