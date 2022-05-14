// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import React from 'react';
import {
    CreateButton,
    useGetList,
    useRecordContext,
    useTranslate,
    TopToolbar,
} from 'react-admin';

import { FormTab } from '../common';
import { EntityAssignmentForm } from '../entities';
import icons from '../icons';

const CloudletFormTab = (props): JSX.Element | null => {
    const record = useRecordContext();
    if (!record || record.id == null) {
        return null;
    }
    const { isLoading, total } = useGetList('cloudlets');
    const translate = useTranslate();
    let label = translate('label.broker.tabs.cloudlets');
    if (!isLoading) {
        label += ` (${total})`;
    }
    return (
        <FormTab label={label} {...props} icon={React.createElement(icons.cloudlets)}>
            <TopToolbar
                sx={{
                    width: '100%',
                }}
            >
                <CreateButton
                    resource='cloudlets'
                    label='label.entity.button.add_cloudlet'
                />
            </TopToolbar>
            <EntityAssignmentForm reference='cloudlets' source='cloudlets' itemResource='cloudlets' />
        </FormTab>
    );
};

export default CloudletFormTab;
