"use client"
import React, {useState} from 'react';
import Cookies from "universal-cookie";
import {Button, message, Upload, UploadProps} from 'antd';
import type {UploadFile} from 'antd/es/upload/interface';
import {UploadOutlined} from '@ant-design/icons';
import {uploadPhotos} from '@/services/photo';

interface IPhoto {
    id: string,
    name: string,
    downloadUrl: string,
    type: string,
    size: number
}

type PhotoUploaderProps = {
    setPhotoIds: React.Dispatch<React.SetStateAction<string[]>>
}

const PhotoUploader: React.FC<PhotoUploaderProps> = ({ setPhotoIds }) => {
    const [photos, setPhotos] = useState<UploadFile[]>([]);
    const [isLoading, setIsLoading] = useState<boolean>(false);
    const [messageApi, contextHolder] = message.useMessage();
    const key = 'uploads';
    const cookies = new Cookies();

    const handleUpload = async () => {
        setIsLoading(true)
        messageApi.open({
            key,
            type: 'loading',
            content: 'Photos are loading...',
        });

        const formData: FormData = new FormData();
        photos.forEach((photo) => {
            formData.append('photos', photo as any);
        });

        const accessToken = await cookies.get("jwt-access-token");

        try {
            const response = await uploadPhotos(formData, accessToken);

            if (response?.success) {
                response?.data.forEach((photo: IPhoto) => {
                    setPhotoIds(photoIds => [...photoIds, photo.id]);
                })
                setIsLoading(false)
                setTimeout(() => {
                    messageApi.open({
                        key,
                        type: 'success',
                        content: 'Photos uploaded successfully',
                        duration: 2,
                    });
                }, 1000);
                setPhotos([]);
            } else {
                setIsLoading(false)
                setTimeout(() => {
                    messageApi.open({
                        key,
                        type: 'error',
                        content: 'Failed to upload photos',
                        duration: 2,
                    });
                }, 1000);
            }
        } catch (error) {
            setIsLoading(false)
            setTimeout(() => {
                messageApi.open({
                    key,
                    type: 'error',
                    content: 'An error occurred while uploading photos',
                    duration: 2,
                });
            }, 1000);
        }
    }

    const photoUploaderProps: UploadProps = {
        name: 'photos',
        multiple: true,
        onChange: ({file, fileList}) => {
            console.log(file, fileList);
        },
        onDrop: (e) => {
            console.log('Dropped files:', e.dataTransfer.files);
        },
        beforeUpload: (file) => {
            setPhotos(prevList => [...prevList, file]);
            return false;
        },
        onRemove: (file) => {
            setPhotos(prevList => prevList.filter(item => item.uid !== file.uid));
        },
    };

    return (
        <>
            {contextHolder}
            <div style={{width: '100%', height: 'auto'}}>
                <Upload.Dragger
                    listType="picture"
                    showUploadList={true}
                    {...photoUploaderProps}
                >
                    <p className="ant-upload-drag-icon">
                        <UploadOutlined/>
                    </p>
                    <p className="ant-upload-text">Click or drag file to this area to upload</p>
                    <p className="ant-upload-hint">Supports: JPEG, JPG, PNG, GIF, BMP, WEBP, TIFF, SVG+XML</p>
                </Upload.Dragger>
                <Button
                    type="primary"
                    onClick={handleUpload}
                    disabled={photos.length === 0}
                    style={{
                        width: "20%",
                        margin: "10px auto",
                        fontSize: '16px',
                        paddingBottom: 6,
                        display: "flex",
                        alignItems: "center"
                }}
                    loading={isLoading}
                >
                    {photos.length > 1 ? 'Upload Photos' : 'Upload Photo'}
                </Button>
            </div>
        </>
    );
};

export default PhotoUploader;
