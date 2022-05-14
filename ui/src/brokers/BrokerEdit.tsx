// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import React from 'react';
import {
    Edit,
} from 'react-admin';

import { EditPageToolbar } from '../common';

import BrokerForm from './BrokerForm';

const BrokerEdit = (props): JSX.Element => (
    <Edit
        redirect='list'
        actions={<EditPageToolbar />}
        {...props}
    >
        <BrokerForm {...props} />
    </Edit>
);

export default BrokerEdit;
