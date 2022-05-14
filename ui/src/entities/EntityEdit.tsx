// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import React from 'react';
import {
    Edit, useResourceContext,
} from 'react-admin';

import { EditPageToolbar } from '../common';

import EntityForm from './EntityForm';
import DatacenterForm from './DatacenterForm';

const EntityEdit = (props): JSX.Element => {
    const resource = useResourceContext(props);

    return (
        <Edit
            actions={<EditPageToolbar />}
            redirect='list'
            {...props}
        >
            {
                resource === 'datacenters' ? (
                    <DatacenterForm {...props} />
                ) : (
                    <EntityForm {...props} edit />
                )
            }
        </Edit>
    );
};

export default EntityEdit;
