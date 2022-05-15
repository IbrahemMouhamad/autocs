// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import React from 'react';
import InfoIcon from '@mui/icons-material/Info';
import {
    EditButton,
} from 'react-admin';
import DefaultModal from '../DefaultModal';

const GridActionToolbar = ({ DetailsComponent, ...props }): JSX.Element => {
    const { children } = props;

    return (
        <>
            <DefaultModal icon={<InfoIcon />} action='action.info'>
                <DetailsComponent />
            </DefaultModal>
            <EditButton />
            {children}
        </>
    );
};

export default GridActionToolbar;
