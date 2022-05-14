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

import BrokerForm from './BrokerForm';

const BrokerCreateToolbar = (props): JSX.Element => (
    <Toolbar {...props}>
        <SaveButton
            label='label.broker.button.create'
            icon={<ArrowRightIcon />}
        />
    </Toolbar>
);

const BrokerCreate = (props): JSX.Element => (
    <Create
        actions={<CreatePageToolbar />}
        redirect='edit'
        {...props}
    >
        <BrokerForm toolbar={<BrokerCreateToolbar />} />
    </Create>
);

export default BrokerCreate;
