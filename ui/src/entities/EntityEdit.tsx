// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import React from 'react';
import {
    Edit,
} from 'react-admin';

import EntityForm from './EntityForm';

const EntityEdit = (props): JSX.Element => (
    <Edit {...props} redirect='list' mutationMode='optimistic'>
        <EntityForm {...props} edit />
    </Edit>
);

export default EntityEdit;
