// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import React, { useState } from 'react';
import InfoIcon from '@mui/icons-material/Info';
import {
    EditButton,
} from 'react-admin';
import DefaultModal from '../DefaultModal';

const GridActionToolbar = ({ DetailsComponent, ...props }): JSX.Element => {
    const [showInfo, setShowInfo] = useState(false);
    const { children, hasEdit } = props;

    return (
        <>
            {
                DetailsComponent &&
                (
                    <DefaultModal
                        icon={<InfoIcon />}
                        action='action.info'
                        open={showInfo}
                        setOpen={setShowInfo}
                    >
                        <DetailsComponent />
                    </DefaultModal>
                )
            }
            {
                hasEdit &&
                (
                    <EditButton />
                )
            }
            {children}
        </>
    );
};

export default GridActionToolbar;
