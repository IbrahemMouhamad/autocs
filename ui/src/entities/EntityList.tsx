// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import React from 'react';
import {
    SingleFieldList,
    ChipField,
    ArrayField,
    useResourceContext,
} from 'react-admin';
import { DefaultListPage, GridActionToolbar } from '../common';

import EntityDetails from './EntityDetails';

const EntityList = (props): JSX.Element => {
    const resource = useResourceContext(props);

    return (
        <DefaultListPage>
            {
                resource === 'datacenters' && (
                    <ArrayField source='hosts'>
                        <SingleFieldList resource='hosts'>
                            <ChipField source='name' />
                        </SingleFieldList>
                    </ArrayField>
                )
            }
            <GridActionToolbar DetailsComponent={EntityDetails} hasEdit />
        </DefaultListPage>
    );
};

export default EntityList;
