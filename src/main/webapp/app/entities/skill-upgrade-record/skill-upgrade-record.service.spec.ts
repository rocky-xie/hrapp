import { beforeEach, describe, expect, it, vi } from 'vitest';

import axios from 'axios';
import dayjs from 'dayjs';

import { DATE_FORMAT } from '@/shared/composables/date-format';
import { SkillUpgradeRecord } from '@/shared/model/skill-upgrade-record.model';

import SkillUpgradeRecordService from './skill-upgrade-record.service';

const error = {
  response: {
    status: null,
    data: {
      type: null,
    },
  },
};

const axiosStub = {
  get: vi.spyOn(axios, 'get'),
  post: vi.spyOn(axios, 'post'),
  put: vi.spyOn(axios, 'put'),
  patch: vi.spyOn(axios, 'patch'),
  delete: vi.spyOn(axios, 'delete'),
};

describe('Service Tests', () => {
  describe('SkillUpgradeRecord Service', () => {
    let service: SkillUpgradeRecordService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new SkillUpgradeRecordService();
      currentDate = new Date();
      elemDefault = new SkillUpgradeRecord(123, 'NEW_SKILL', currentDate, 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA');
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = { changeDate: dayjs(currentDate).format(DATE_FORMAT), ...elemDefault };
        axiosStub.get.mockResolvedValue({ data: returnedFromService });

        return service.find(123).then(res => {
          expect(res).toMatchObject(elemDefault);
        });
      });

      it('should not find an element', async () => {
        axiosStub.get.mockRejectedValue(error);
        return service
          .find(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should create a SkillUpgradeRecord', async () => {
        const returnedFromService = { id: 123, changeDate: dayjs(currentDate).format(DATE_FORMAT), ...elemDefault };
        const expected = { changeDate: currentDate, ...returnedFromService };

        axiosStub.post.mockResolvedValue({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a SkillUpgradeRecord', async () => {
        axiosStub.post.mockRejectedValue(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a SkillUpgradeRecord', async () => {
        const returnedFromService = {
          changeType: 'BBBBBB',
          changeDate: dayjs(currentDate).format(DATE_FORMAT),
          reason: 'BBBBBB',
          beforeLevelLabel: 'BBBBBB',
          afterLevelLabel: 'BBBBBB',
          evidence: 'BBBBBB',
          comment: 'BBBBBB',
          ...elemDefault,
        };

        const expected = { changeDate: currentDate, ...returnedFromService };
        axiosStub.put.mockResolvedValue({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a SkillUpgradeRecord', async () => {
        axiosStub.put.mockRejectedValue(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a SkillUpgradeRecord', async () => {
        const patchObject = {
          changeType: 'BBBBBB',
          afterLevelLabel: 'BBBBBB',
          evidence: 'BBBBBB',
          comment: 'BBBBBB',
          ...new SkillUpgradeRecord(),
        };
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = { changeDate: currentDate, ...returnedFromService };
        axiosStub.patch.mockResolvedValue({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a SkillUpgradeRecord', async () => {
        axiosStub.patch.mockRejectedValue(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of SkillUpgradeRecord', async () => {
        const returnedFromService = {
          changeType: 'BBBBBB',
          changeDate: dayjs(currentDate).format(DATE_FORMAT),
          reason: 'BBBBBB',
          beforeLevelLabel: 'BBBBBB',
          afterLevelLabel: 'BBBBBB',
          evidence: 'BBBBBB',
          comment: 'BBBBBB',
          ...elemDefault,
        };
        const expected = { changeDate: currentDate, ...returnedFromService };
        axiosStub.get.mockResolvedValue([returnedFromService]);
        return service.retrieve({ sort: {}, page: 0, size: 10 }).then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of SkillUpgradeRecord', async () => {
        axiosStub.get.mockRejectedValue(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a SkillUpgradeRecord', async () => {
        axiosStub.delete.mockResolvedValue({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a SkillUpgradeRecord', async () => {
        axiosStub.delete.mockRejectedValue(error);

        return service
          .delete(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });
    });
  });
});
