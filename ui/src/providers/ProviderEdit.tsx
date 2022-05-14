// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import React from 'react';
import {
    Edit,
} from 'react-admin';

import { EditPageToolbar } from '../common';

import ProviderForm from './ProviderForm';

const ProviderEdit = (props): JSX.Element => (
    <Edit
        actions={<EditPageToolbar />}
        redirect='list'
        {...props}
    >
        <ProviderForm {...props} />
    </Edit>
);

export default ProviderEdit;
