// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import React from 'react';
import {
    BooleanInput,
    required,
    minValue,
    maxValue,
} from 'react-admin';
import { makeStyles } from '@mui/styles';
import Typography from '@mui/material/Typography';
import CheckCircleIcon from '@mui/icons-material/CheckCircle';

import {
    IconWithTooltip,
    TextInput,
    SelectInput,
    NumberInput,
} from '../common';

const useStyles = makeStyles(
    (theme) => ({
        container: {
            display: 'flex',
            flexWrap: 'wrap',
            width: '100%',
        },
        heading: {
            fontSize: theme.typography.pxToRem(15),
            padding: '10px 0',
        },
        column: {
            flexGrow: 1,
            width: '23%',
            marginRight: '10px',
        },
        booleanInputContainer: {
            display: 'flex',
            justifyContent: 'center',
        },
        booleanInput: {
            '& label': {
                justifyContent: 'center',
            },
        },
    }),
    {
        name: 'ConfigurationInputs',
    },
);

const ConfigurationInputs = ({
    config,
    name,
    title,
}): JSX.Element => {
    const classes = useStyles();

    const defaultParams = (key: string): any => ({
        helpText: config[key].description,
        source: `${name}.${key}`,
        label: key,
        defaultValue: config[key].defaultValue,
    });

    return (
        <>
            <Typography className={classes.heading}>{title}</Typography>
            <div className={classes.container}>
                {
                    Object.keys(config).map((key) => {
                        let input;
                        const disabled = false;
                        switch (config[key].type) {
                            case 'int':
                            case 'float': {
                                input = (
                                    <NumberInput
                                        validate={config[key].is_required ?
                                            [
                                                required(),
                                                minValue(config[key].min_value),
                                                maxValue(config[key].max_value),
                                            ] : [
                                                minValue(config[key].min_value),
                                                maxValue(config[key].max_value),
                                            ]}
                                        {...defaultParams(key)}
                                    />
                                );
                                break;
                            }
                            case 'str': {
                                input = (
                                    <TextInput
                                        validate={config[key].is_required ? [required()] : []}
                                        {...defaultParams(key)}
                                    />
                                );
                                break;
                            }
                            case 'select': {
                                input = (
                                    <SelectInput
                                        choices={
                                            config[key].options.map(
                                                (value) => ({
                                                    id: value,
                                                    name: value,
                                                }),
                                            )
                                        }
                                        defaultValue={disabled ? '' : config[key].defaultValue}
                                        validate={config[key].required ? [required()] : []}
                                        disabled={disabled}
                                        {...defaultParams(key)}
                                    />
                                );
                                break;
                            }
                            case 'boolean': {
                                input = (
                                    <div className={classes.booleanInputContainer}>
                                        <BooleanInput
                                            validate={config[key].required ? [required()] : []}
                                            options={{
                                                checkedIcon: <CheckCircleIcon />,
                                            }}
                                            className={classes.booleanInput}
                                            {...defaultParams(key)}
                                        />
                                        <IconWithTooltip helpText={config[key].description} />
                                    </div>
                                );
                                break;
                            }
                            default: {
                                break;
                            }
                        }
                        return (
                            <div key={key} className={classes.column}>
                                {input}
                            </div>
                        );
                    })
                }
            </div>
        </>
    );
};

export default ConfigurationInputs;
