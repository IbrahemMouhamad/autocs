// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import React from 'react';
import {
    SingleFieldList,
    ChipField,
    ArrayField,
} from 'react-admin';
import { DefaultListPage, GridActionToolbar } from '../common';
import { EntityDetails } from '../entities';

const BrokerList = (): JSX.Element => (
    <DefaultListPage>
        <ArrayField source='vms'>
            <SingleFieldList resource='vms'>
                <ChipField source='name' />
            </SingleFieldList>
        </ArrayField>
        <ArrayField source='cloudlets'>
            <SingleFieldList resource='cloudlets'>
                <ChipField source='name' />
            </SingleFieldList>
        </ArrayField>
        <GridActionToolbar DetailsComponent={EntityDetails} hasEdit />
    </DefaultListPage>
);

export default BrokerList;
