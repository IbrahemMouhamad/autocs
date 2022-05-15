// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import React from 'react';
import {
    SingleFieldList,
    ChipField,
    ArrayField,
    ReferenceField,
} from 'react-admin';
import { DefaultListPage, GridActionToolbar } from '../common';
import { EntityDetails } from '../entities';

import RunButton from './buttons/RunButton';

const ScenarioList = (): JSX.Element => (
    <DefaultListPage>
        <ReferenceField source='provider.id' reference='providers'>
            <ChipField source='name' />
        </ReferenceField>
        <ArrayField source='brokers'>
            <SingleFieldList resource='brokers'>
                <ChipField source='name' />
            </SingleFieldList>
        </ArrayField>
        <GridActionToolbar DetailsComponent={EntityDetails}>
            <RunButton />
        </GridActionToolbar>
    </DefaultListPage>
);

export default ScenarioList;
