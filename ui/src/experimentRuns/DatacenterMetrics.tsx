// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import React, { useState } from 'react';
import Grid from '@mui/material/Grid';
import get from 'lodash/get';
import { useRecordContext, SelectInput, Form } from 'react-admin';

import {
    CpuUtilization,
    RamUtilization,
    BwUtilization,
    ActiveHostsNumber,
} from './metrics/datacenter';

const DatacenterSelector = ({ datacenters, setSelectedDatacenter }): JSX.Element => (
    <Form>
        <SelectInput
            label='Select'
            source='datacenter'
            choices={datacenters}
            inputProps={{
                onChange: (event) => {
                    setSelectedDatacenter((event.target as HTMLTextAreaElement).value);
                },
            }}
            optionValue='name'
        />
    </Form>
);

const DatacenterMetrics = (): JSX.Element | null => {
    const [selectedDatacenter, setSelectedDatacenter] = useState('');
    const record = useRecordContext();
    if (!record || record.id == null || record.metrics == null || record.metrics.datacentersMetrics == null) {
        return null;
    }

    const datacenters = get(record, 'scenario.provider.datacenters').map((datacenter) => ({ id: datacenter.id, name: datacenter.name }));
    const metrics = get(record, 'metrics.datacentersMetrics').find((datacenterMetrics) => datacenterMetrics.name === selectedDatacenter);

    return datacenters ? (
        <>
            <DatacenterSelector
                datacenters={datacenters}
                setSelectedDatacenter={setSelectedDatacenter}
            />
            {
                selectedDatacenter && metrics && (
                    <>
                        <CpuUtilization data={metrics.history} />
                        <Grid container>
                            <Grid item md={6} sm={12} xs={12}>
                                <RamUtilization data={metrics.history} />
                            </Grid>
                            <Grid item md={6} sm={12} xs={12}>
                                <BwUtilization data={metrics.history} />
                            </Grid>
                            <Grid item md={6} sm={12} xs={12}>
                                <ActiveHostsNumber data={metrics.history} />
                            </Grid>
                        </Grid>
                    </>
                )
            }
        </>
    ) : null;
};

export default DatacenterMetrics;
