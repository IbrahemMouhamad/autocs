// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import React from 'react';
import RocketLaunchIcon from '@mui/icons-material/RocketLaunch';
import { useRecordContext } from 'react-admin';

import { DefaultModal } from '../../common';

import { RunCreate } from '../../experimentRuns';

const RunButton = (): JSX.Element | null => {
    const record = useRecordContext();
    if (!record || record.id == null) {
        return null;
    }
    const { id } = record;

    return (
        <DefaultModal
            icon={<RocketLaunchIcon />}
            action='label.scenario.action.run'
        >
            <RunCreate resource='runs' scenario={id} />
        </DefaultModal>
    );
};

export default RunButton;
