import React from "react";
import {Modal} from "antd";
import CreateAdvertSteps from "@/components/createAdvertSteps";

type CreateAdvertModalProps = {
    isOpen: boolean,
    setIsOpen: React.Dispatch<React.SetStateAction<boolean>>
}

const CreateAdvertModal: React.FC<CreateAdvertModalProps> = ({ isOpen, setIsOpen }) => {
    return (
        <>
            <Modal
                open={isOpen}
                onCancel={() => setIsOpen(false)}
                footer={false}
                width={"90%"}
                height={"auto"}
            >
                <CreateAdvertSteps setIsOpen={setIsOpen} />
            </Modal>
        </>
    )
}

export default CreateAdvertModal