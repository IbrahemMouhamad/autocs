// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import React from 'react';
import {
    Create,
    Toolbar,
    SaveButton,
} from 'react-admin';
import ArrowRightIcon from '@mui/icons-material/ArrowRight';

import { CreatePageToolbar } from '../common';
import ProviderForm from './ProviderForm';

const ProviderCreateToolbar = (props): JSX.Element => (
    <Toolbar {...props}>
        <SaveButton
            label='label.provider.button.create'
            icon={<ArrowRightIcon />}
        />
    </Toolbar>
);

const ProviderCreate = (props): JSX.Element => (
    <Create
        actions={<CreatePageToolbar />}
        redirect='edit'
        {...props}
    >
        <ProviderForm toolbar={<ProviderCreateToolbar />} />
    </Create>
);

export default ProviderCreate;
