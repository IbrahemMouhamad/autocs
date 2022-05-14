// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import React from 'react';
import {
    Create,
    useResourceContext,
} from 'react-admin';

import { CreatePageToolbar } from '../common';

import EntityForm from './EntityForm';
import DatacenterForm from './DatacenterForm';

const EntityCreate = (props): JSX.Element => {
    const resource = useResourceContext(props);

    return (
        <Create
            actions={<CreatePageToolbar />}
            redirect='edit'
            {...props}
        >
            {
                resource === 'datacenters' ? (
                    <DatacenterForm {...props} />
                ) : (
                    <EntityForm {...props} />
                )
            }
        </Create>
    );
};

export default EntityCreate;
