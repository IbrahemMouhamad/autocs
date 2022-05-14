// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import React from 'react';
import {
    EditButton,
} from 'react-admin';
import InfoModal from '../InfoModal';

const GridActionToolbar = ({ children = undefined, DetailsComponent }): JSX.Element => (
    <>
        <InfoModal><DetailsComponent /></InfoModal>
        <EditButton />
        {children}
    </>
);

export default GridActionToolbar;
