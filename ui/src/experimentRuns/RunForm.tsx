// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import React from 'react';
import RocketLaunchIcon from '@mui/icons-material/RocketLaunch';
import {
    Form,
    Toolbar,
    SaveButton,
    TextInput,
} from 'react-admin';

import { BasicConfigurationForm, AdvancedConfigurationForm } from '../entities';

const RunForm = ({ scenario, ...props }): JSX.Element => (
    <Form {...props}>
        <BasicConfigurationForm />
        <AdvancedConfigurationForm />
        <TextInput
            source='scenario.id'
            defaultValue={scenario}
            style={{ display: 'none' }}
        />
        <Toolbar
            sx={{
                flexDirection: 'row-reverse',
            }}
        >
            <SaveButton
                label='label.run.create.save'
                endIcon={<RocketLaunchIcon />}
                icon={<> </>}
            />
        </Toolbar>
    </Form>
);

export default RunForm;
