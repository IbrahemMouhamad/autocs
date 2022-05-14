// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import React from 'react';
import CloseTwoToneIcon from '@mui/icons-material/CloseTwoTone';
import { TopToolbar, ListButton } from 'react-admin';

const CreatePageToolbar = ({ children = undefined }): JSX.Element => (
    <TopToolbar>
        <ListButton label='ra.action.close' icon={<CloseTwoToneIcon />} />
        {children}
    </TopToolbar>
);

export default CreatePageToolbar;
