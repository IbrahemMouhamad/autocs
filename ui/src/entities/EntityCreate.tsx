// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import React from 'react';
import {
    Create,
} from 'react-admin';

import EntityForm from './EntityForm';

const EntityCreate = (props): JSX.Element => (
    <Create {...props} redirect='list'>
        <EntityForm {...props} />
    </Create>
);

export default EntityCreate;
