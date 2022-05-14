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

const VmFormTab = (props): JSX.Element | null => {
    const record = useRecordContext();
    if (!record || record.id == null) {
        return null;
    }
    const { isLoading, total } = useGetList('vms');
    const translate = useTranslate();
    let label = translate('label.broker.tabs.vms');
    if (!isLoading) {
        label += ` (${total})`;
    }
    return (
        <FormTab label={label} {...props} icon={React.createElement(icons.vms)}>
            <TopToolbar
                sx={{
                    width: '100%',
                }}
            >
                <CreateButton
                    resource='vms'
                    label='label.entity.button.add_vm'
                />
            </TopToolbar>
            <EntityAssignmentForm reference='vms' source='vms' itemResource='vms' />
        </FormTab>
    );
};

export default VmFormTab;
